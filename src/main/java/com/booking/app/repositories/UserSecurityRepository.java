package com.booking.app.repositories;

import com.booking.app.entity.UserSecurity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserSecurityRepository extends JpaRepository<UserSecurity,UUID>{
    Optional<UserSecurity> findByUsername(String name);
    Optional<UserSecurity> findByEmail(String email);

//    @Modifying
//    @Query(value = "UPDATE UserSecurity u SET u.enabled = true WHERE u.id = :userId")
//    void enableUserById(@Param("userId") UUID userId);

    @Modifying
    @Query(value = "UPDATE UserSecurity u SET u.password = :password WHERE u.id = :userId")
    void changePasswordById(@Param("userId") UUID userId, @Param("password") String password);

    @Modifying
    @Query("DELETE FROM UserSecurity u WHERE u.id = :pid")
    void deleteByPid(@Param("pid") UUID theId);

    @Modifying
    @Query(value = "UPDATE UserSecurity u SET u.enabled = true, u.accountNonExpired = true, u.accountNonLocked = true, u.credentialsNonExpired = true WHERE u.id = :userId")
    void enableAllBooleansForUser(@Param("userId") UUID userId);

}