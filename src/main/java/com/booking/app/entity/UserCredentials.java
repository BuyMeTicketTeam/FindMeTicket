package com.booking.app.entity;

import com.booking.app.enums.EnumProvider;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.UUID;

@Entity
@Table(name = "user_credentials",
        indexes = {@Index(name = "idx_email", columnList = "email", unique = true)})

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@ToString
public class UserCredentials implements UserDetails {

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

    @JoinColumn(referencedColumnName = "id", name = "user_id")
    @OneToOne(cascade = CascadeType.ALL)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider")
    private EnumProvider provider;

    private boolean accountNonExpired;

    private boolean accountNonLocked;

    private boolean credentialsNonExpired;

    private boolean enabled;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRole().getEnumRole().getSimpleGrantedAuthorities();
    }

}

