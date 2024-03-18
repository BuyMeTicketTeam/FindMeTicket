package com.booking.app.repositories;

import com.booking.app.entity.Role;

import com.booking.app.enums.EnumRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findRoleByEnumRole(EnumRole enumRole);

}
