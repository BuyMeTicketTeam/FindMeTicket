package com.booking.app.repositories;

import com.booking.app.entity.User;
import com.booking.app.entity.UserSecurity;
import org.mapstruct.control.MappingControl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.UUID;

@Repository
public interface UserDataRepository extends JpaRepository<User, UUID> {
    @Modifying
    @Query("DELETE FROM User u WHERE u.id = :userId")
    void deleteUserById(@Param("userId") UUID userId);

}
