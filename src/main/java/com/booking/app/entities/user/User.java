package com.booking.app.entities.user;

import com.booking.app.utils.AvatarUtils;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(indexes = {@Index(name = "idx_email", columnList = "email", unique = true)})
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

    private boolean notification = false;

    private boolean accountNonExpired;

    private boolean accountNonLocked;

    private boolean credentialsNonExpired;

    private boolean enabled;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_provider",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "provider_id")
    )
    private Set<AuthProvider> providers = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(referencedColumnName = "id", name = "code_id")
    private ConfirmationCode confirmationCode;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<SearchHistory> history = new ArrayList<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Review review;

    public static User createBasicAdmin(Role role, ConfirmationCode confirmationCode, String email, String password, String username, Boolean notification) throws IOException {
        Set<Role> setRoles = Role.createRoles(role);
        File fi = new File("image/admin_avatar.png");
        byte[] fileContent = Files.readAllBytes(fi.toPath());
        return User.builder()
                .email(email)
                .username(username)
                .password(password)
                .roles(setRoles)
                .confirmationCode(confirmationCode)
                .notification(notification)
                .defaultAvatar(fileContent).build();
    }

    public static User createBasicUser(AuthProvider authProvider, Role role, ConfirmationCode confirmationCode, String email, String password, String username, Boolean notification) {
        Set<Role> setRoles = Role.createRoles(role);
        Set<AuthProvider> setAuthProviders = AuthProvider.createProvider(authProvider);
        byte[] avatarAsBytes = AvatarUtils.createRandomAvatarAsBytes();
        return User.builder()
                .providers(setAuthProviders)
                .email(email)
                .username(username)
                .password(password)
                .roles(setRoles)
                .confirmationCode(confirmationCode)
                .notification(notification)
                .defaultAvatar(avatarAsBytes).build();
    }

    public static User createGoogleUser(AuthProvider authProvider, Role role, String username, String email, String urlPicture) {
        Set<Role> setRoles = Role.createRoles(role);
        Set<AuthProvider> setAuthProviders = AuthProvider.createProvider(authProvider);
        return User.builder()
                .providers(setAuthProviders)
                .username(username)
                .email(email)
                .socialMediaAvatar(urlPicture)
                .roles(setRoles)
                .enabled(true)
                .accountNonExpired(true)
                .credentialsNonExpired(true)
                .accountNonLocked(true)
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .flatMap(r -> r.getGrantedAuthorities().stream())
                .collect(Collectors.toSet());
    }

}
