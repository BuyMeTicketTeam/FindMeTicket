package com.booking.app.services.impl;

import com.booking.app.dto.LoginDTO;
import com.booking.app.dto.RegistrationDTO;
import com.booking.app.entity.Role;
import com.booking.app.entity.User;
import com.booking.app.entity.UserSecurity;
import com.booking.app.entity.enums.EnumRole;
import com.booking.app.exception.exception.UserAlreadyExistAuthenticationException;
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

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserSecurityServiceImpl implements UserDetailsService, UserSecurityService {

    private final UserSecurityRepository userSecurityRepository;
    private final RoleRepository roleRepository;
    private final UserDataRepository userDataRepository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserSecurity userSecurity = userSecurityRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User %s not found", email)));
        return userSecurity;
    }





    @Override
    public LoginDTO register(RegistrationDTO securityDTO) throws UserAlreadyExistAuthenticationException {

        if(userSecurityRepository.findByEmail(securityDTO.getEmail()).isPresent()) {
            throw new UserAlreadyExistAuthenticationException("We’re sorry. This email already exists…");
        }

        String hashPwd = passwordEncoder.encode(securityDTO.getPassword());
        UserSecurity securityEntity = mapper.toEntityRegistration(securityDTO);

        securityEntity.setPassword(hashPwd);

        Role role = roleRepository.findByEnumRole(EnumRole.USER);
        User user = new User();

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

    @Override
    public Optional<UserSecurity> findByEmail(String email) {
       return userSecurityRepository.findByEmail(email);
    }


}
