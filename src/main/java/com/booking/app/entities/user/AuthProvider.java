package com.booking.app.entities.user;

import jakarta.persistence.*;
import lombok.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "provider")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class AuthProvider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Short id;

    @Enumerated(EnumType.STRING)
    private AuthProviderType type;

    public static Set<AuthProvider> createProvider(AuthProvider... authProviders) {
        return new HashSet<>(Arrays.asList(authProviders));
    }

    public enum AuthProviderType {
        BASIC,
        GOOGLE,
        FACEBOOK
    }

}
