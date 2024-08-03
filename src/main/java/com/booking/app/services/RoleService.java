package com.booking.app.services;

import com.booking.app.entities.user.Role;

public interface RoleService {

    Role findByType(Role.RoleType type);

}
