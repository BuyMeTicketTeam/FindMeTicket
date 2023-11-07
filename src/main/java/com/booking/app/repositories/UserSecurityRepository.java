package com.booking.app.repositories;

import com.booking.app.entity.UserSecurity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

import java.io.Serializable;
@Repository
public interface UserSecurityRepository extends JpaRepository<UserSecurity,UUID>{
    Optional<UserSecurity> findByUsername(String name);
    Optional<UserSecurity> findByEmail(String email);
}
