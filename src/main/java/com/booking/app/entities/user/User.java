package com.booking.app.entities.user;

import com.booking.app.constants.AuthProvider;
import com.booking.app.constants.RoleType;
import com.booking.app.utils.AvatarUtils;
import io.hypersistence.utils.hibernate.type.array.EnumArrayType;
import io.hypersistence.utils.hibernate.type.array.internal.AbstractArrayType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "user", indexes =
        {
                @Index(name = "idx_email", columnList = "email", unique = true)
        }
)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EntityListeners(AuditingEntityListener.class)

public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Email
    @Column(unique = true, name = "email")
    private String email;

    @Column
    private String password;

    @Column
    private String username;

    @CreatedDate
    private LocalDate registrationDate;

    @Column(unique = true)
    private String phoneNumber;

    @Column(columnDefinition = "BYTEA")
    private byte[] defaultAvatar;

    private String socialMediaAvatar;

    @Builder.Default
    private boolean notification = false;

    @Column(name = "expired")
    @Builder.Default
    private boolean accountNonExpired = false;

    @Column(name = "locked")
    @Builder.Default
    private boolean accountNonLocked = false;

    @Column(name = "credentials_expired")
    @Builder.Default
    private boolean credentialsNonExpired = false;

    @Column(name = "enabled")
    @Builder.Default
    private boolean enabled = false;

    @Type(
            value = EnumArrayType.class,
            parameters = @org.hibernate.annotations.Parameter(
                    name = AbstractArrayType.SQL_ARRAY_TYPE,
                    value = "auth_provider"
            )
    )
    @Column(
            name = "providers",
            columnDefinition = "auth_provider[]"
    )
    private AuthProvider[] providers;

    @Type(
            value = EnumArrayType.class,
            parameters = @org.hibernate.annotations.Parameter(
                    name = AbstractArrayType.SQL_ARRAY_TYPE,
                    value = "role"
            )
    )
    @Column(
            name = "roles",
            columnDefinition = "role[]"
    )
    private RoleType[] roles;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(referencedColumnName = "id", name = "code_id")
    private ConfirmationCode confirmationCode;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<SearchHistory> history = new ArrayList<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Review review;

    public static User createBasicAdmin(ConfirmationCode confirmationCode, String email, String password, String username, Boolean notification) throws IOException {
        File adminAvatar = new File("image/admin_avatar.png");
        byte[] fileContent = Files.readAllBytes(adminAvatar.toPath());
        return User.builder()
                .email(email)
                .username(username)
                .password(password)
                .roles(new RoleType[]{RoleType.ADMIN})
                .confirmationCode(confirmationCode)
                .notification(notification)
                .defaultAvatar(fileContent).build();
    }

    public static User createBasicUser(ConfirmationCode confirmationCode, String email, String password, String username, Boolean notification) {
        byte[] avatarAsBytes = AvatarUtils.createRandomAvatarAsBytes();
        return User.builder()
                .providers(new AuthProvider[]{AuthProvider.BASIC})
                .email(email)
                .username(username)
                .password(password)
                .roles(new RoleType[]{RoleType.USER})
                .confirmationCode(confirmationCode)
                .notification(notification)
                .defaultAvatar(avatarAsBytes).build();
    }

    public static User createGoogleUser(String username, String email, String urlPicture) {
        return User.builder()
                .providers(new AuthProvider[]{AuthProvider.GOOGLE})
                .username(username)
                .email(email)
                .socialMediaAvatar(urlPicture)
                .roles(new RoleType[]{RoleType.USER})
                .enabled(true)
                .accountNonExpired(true)
                .credentialsNonExpired(true)
                .accountNonLocked(true)
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(roles).map(role -> new SimpleGrantedAuthority(("ROLE_" + role))).collect(Collectors.toSet());
    }

}
