package com.booking.app.repositories;

import com.booking.app.entity.VerifyEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface VerifyEmailRepository extends JpaRepository<VerifyEmail, UUID> {
    VerifyEmail findByToken(String token);

}
