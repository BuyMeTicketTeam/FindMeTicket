package com.booking.app.repositories;

import com.booking.app.entities.user.AuthProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProviderRepository extends JpaRepository<AuthProvider, UUID> {
    AuthProvider findByType(AuthProvider.AuthProviderType type);
}
