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
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserSecurityServiceImpl implements UserDetailsService, UserSecurityService {

    private final UserSecurityRepository userSecurityRepository;
    private final RoleRepository roleRepository;
    private final UserDataRepository userDataRepository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserSecurity userSecurity = userSecurityRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User %s not found", email)));
        return userSecurity;
    }


    @Transactional
    @Override
    public LoginDTO register(RegistrationDTO securityDTO) throws UserAlreadyExistAuthenticationException {
        if (userSecurityRepository.findByEmail(securityDTO.getEmail()).isPresent()) {
            throw new UserAlreadyExistAuthenticationException("We’re sorry. This email already exists…");
        }

        UserSecurity securityEntity = mapper.toEntityRegistration(securityDTO);
        securityEntity.setPassword(passwordEncoder.encode(securityDTO.getPassword()));

        User user = createNewRegisteredUser(securityEntity);
        user.setSecurity(securityEntity);
        user.getSecurity().setUser(user);

        userDataRepository.save(user);

        return mapper.toDtoLogin(securityEntity);

    }

    private User createNewRegisteredUser(UserSecurity userSecurity) {
        // TODO might not to address to DB
        Role role = roleRepository.findByEnumRole(EnumRole.USER);
        return User.builder()
                .registrationDate(LocalDate.now())
                .security(userSecurity)
                .role(role)

                .build();
    }

    @Override
    public Optional<UserSecurity> findByEmail(String email) {
        return userSecurityRepository.findByEmail(email);
    }

}
