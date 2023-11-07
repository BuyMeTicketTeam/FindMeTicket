package com.booking.app.repositories;

import com.booking.app.entity.Role;
import com.booking.app.enums.EnumRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Role findByEnumRole(EnumRole enumRole);
}
