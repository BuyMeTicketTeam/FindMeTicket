package com.booking.app.repositories;

import com.booking.app.entity.Role;
import com.booking.app.entity.enums.EnumRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Role findByEnumRole(EnumRole enumRole);

}
