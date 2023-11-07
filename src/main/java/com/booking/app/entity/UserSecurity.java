package com.booking.app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.UUID;


@Entity
@Table(name = "user_security")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UserSecurity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Email
    @Column(unique = true,name = "email")
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(unique = true)
    private String username;

    @JoinColumn(referencedColumnName = "id", name = "user_id")
    @OneToOne
    private User user;

    private boolean accountNonExpired;

    private boolean accountNonLocked;

    private boolean credentialsNonExpired;

    private boolean enabled;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRole().getEnumRole().getSimpleGrantedAuthorities();
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

