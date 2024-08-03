package com.booking.app.services.impl.user;


import com.booking.app.entities.user.Role;
import com.booking.app.repositories.RoleRepository;
import com.booking.app.services.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role findByType(Role.RoleType type) {
        return roleRepository.findByType(type);
    }

}
