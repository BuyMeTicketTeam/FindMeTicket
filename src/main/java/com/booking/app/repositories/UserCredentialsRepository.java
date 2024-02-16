package com.booking.app.repositories;

import com.booking.app.entity.UserCredentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserCredentialsRepository extends JpaRepository<UserCredentials, UUID> {

    Optional<UserCredentials> findByEmailOrUsername(String email, String username);

    Optional<UserCredentials> findByEmail(String email);

    @Modifying
    @Query(value = "UPDATE UserCredentials u SET u.password = :password WHERE u.id = :userId")
    void updatePassword(@Param("userId") UUID userId, @Param("password") String password);

    @Modifying
    @Query("DELETE FROM UserCredentials u WHERE u.id = :pid")
    void deleteByPid(@Param("pid") UUID theId);

    @Modifying
    @Query(value = "UPDATE UserCredentials u SET u.enabled = true, u.accountNonExpired = true, u.accountNonLocked = true, u.credentialsNonExpired = true WHERE u.id = :userId")
    void enableUser(@Param("userId") UUID userId);

}