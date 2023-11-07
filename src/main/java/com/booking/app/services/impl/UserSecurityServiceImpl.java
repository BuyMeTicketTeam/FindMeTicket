package com.booking.app.services.impl;

import com.booking.app.controller.dto.LoginDTO;
import com.booking.app.controller.dto.RegistrationDTO;
import com.booking.app.entity.Role;
import com.booking.app.entity.User;
import com.booking.app.entity.UserSecurity;
import com.booking.app.enums.EnumRole;
import com.booking.app.exceptionhandling.exception.UserAlreadyExistAuthenticationException;
import com.booking.app.mapper.UserMapper;
import com.booking.app.repositories.RoleRepository;
import com.booking.app.repositories.UserDataRepository;
import com.booking.app.repositories.UserSecurityRepository;
import com.booking.app.services.UserSecurityService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserSecurityServiceImpl implements UserDetailsService, UserSecurityService {

    private final UserSecurityRepository userSecurityRepository;
    private final RoleRepository roleRepository;
    private final UserDataRepository userDataRepository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserSecurity userSecurity = userSecurityRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User %s not found", username)));
        return userSecurity;
    }


    @Override
    public LoginDTO register(RegistrationDTO securityDTO) throws UserAlreadyExistAuthenticationException {

        if(userSecurityRepository.findByEmail(securityDTO.getEmail()).isPresent()) {
            throw new UserAlreadyExistAuthenticationException("We’re sorry. This email already exists…");
        }

        String hashPwd = passwordEncoder.encode(securityDTO.getPassword());
        //String hashPwd = (securityDTO.getPassword());
        UserSecurity securityEntity = mapper.toEntityRegistration(securityDTO);

        securityEntity.setPassword(hashPwd);

        Role role = roleRepository.findByEnumRole(EnumRole.USER);

        User user = securityEntity.getUser();

        user.setRole(role);
        user.setSecurity(securityEntity);

        user.getSecurity().setUser(user);

        UserSecurity userSecurityEntity = user.getSecurity();
        userSecurityEntity.setAccountNonExpired(true);
        userSecurityEntity.setAccountNonLocked(true);
        userSecurityEntity.setCredentialsNonExpired(true);
        userSecurityEntity.setEnabled(true);

        userDataRepository.save(user);

        return mapper.toDtoLogin(userSecurityEntity);

    }

}
