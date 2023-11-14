package com.booking.app.repositories;

import com.booking.app.entity.ConfirmToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface VerifyEmailRepository extends JpaRepository<ConfirmToken, UUID> {
    ConfirmToken findByToken(String token);

}
