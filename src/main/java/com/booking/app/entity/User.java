package com.booking.app.entity;

import com.booking.app.enums.SocialProvider;
import com.booking.app.util.AvatarGenerator;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users",
        indexes = {@Index(name = "idx_email", columnList = "email", unique = true)})
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

    @Column(name = "registration_date")
    @CreatedDate
    private LocalDate registrationDate;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider")
    private SocialProvider provider;

    @Column(columnDefinition = "BYTEA")
    private byte[] defaultAvatar;

    private String socialMediaAvatar;

    private boolean notification = false;

    private boolean accountNonExpired;

    private boolean accountNonLocked;

    private boolean credentialsNonExpired;

    private boolean enabled;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id", name = "role_id")
    private Role role;

    @JoinColumn(referencedColumnName = "id", name = "token_id")
    @OneToOne(cascade = CascadeType.ALL)
    private ConfirmationCode confirmationCode;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<SearchHistory> history;

    @OneToOne(mappedBy = "user")
    private Review review;

    public static User createBasicUser(Role role, ConfirmationCode confirmationCode, Boolean notification) {
        byte[] avatarAsBytes = AvatarGenerator.createRandomAvatarAsBytes();
        return User.builder()
                .role(role)
                .confirmationCode(confirmationCode)
                .notification(notification)
                .defaultAvatar(avatarAsBytes).build();
    }

    public static User createGoogleUser(Role role, String username, String email, String urlPicture) {
        return User.builder()
                .provider(SocialProvider.GOOGLE)
                .username(username)
                .email(email)
                .socialMediaAvatar(urlPicture)
                .role(role)
                .enabled(true)
                .accountNonExpired(true)
                .credentialsNonExpired(true)
                .accountNonLocked(true)
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRole().getEnumRole().getSimpleGrantedAuthorities();
    }

}
