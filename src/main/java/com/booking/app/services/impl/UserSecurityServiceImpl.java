package com.booking.app.services.impl;

import com.booking.app.dto.EmailDTO;
import com.booking.app.dto.TokenConfirmationDTO;
import com.booking.app.exception.exception.UserNotConfirmedException;
import com.booking.app.dto.RegistrationDTO;
import com.booking.app.entity.Role;
import com.booking.app.entity.User;
import com.booking.app.entity.UserSecurity;
import com.booking.app.entity.VerifyEmail;
import com.booking.app.entity.enums.EnumRole;
import com.booking.app.exception.exception.UserAlreadyExistAuthenticationException;
import com.booking.app.mapper.UserMapper;
import com.booking.app.repositories.RoleRepository;
import com.booking.app.repositories.UserRepository;
import com.booking.app.repositories.UserSecurityRepository;
import com.booking.app.repositories.VerifyEmailRepository;
import com.booking.app.services.MailSenderService;
import com.booking.app.services.UserSecurityService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserSecurityServiceImpl implements UserDetailsService, UserSecurityService {

    private final UserSecurityRepository userSecurityRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final VerifyEmailRepository verifyEmailRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper mapper;
    private final MailSenderService mailService;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserSecurity userSecurity = userSecurityRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User %s not found", email)));
        return userSecurity;
    }

    @Transactional
    @Override
    public EmailDTO register(RegistrationDTO securityDTO) throws UserAlreadyExistAuthenticationException, MessagingException, IOException {
        Optional<UserSecurity> byEmail = userSecurityRepository.findByEmail(securityDTO.getEmail());

        if (byEmail.isPresent() && byEmail.get().isEnabled()) {
            throw new UserAlreadyExistAuthenticationException("We’re sorry. This email already exists");
        }
        if (byEmail.isPresent()) {
            throw new UserNotConfirmedException("User does not exist due to not confirmed email");
        }


        UserSecurity securityEntity = mapper.toEntityRegistration(securityDTO);
        securityEntity.setPassword(passwordEncoder.encode(securityDTO.getPassword()));

        User user = createNewRegisteredUser(securityEntity);
        user.setSecurity(securityEntity);
        user.getSecurity().setUser(user);

        userRepository.save(user);
        mailService.sendEmail(securityEntity.getEmail());


        return mapper.toEmail(securityEntity);

    }

    private User createNewRegisteredUser(UserSecurity userSecurity) {
        Role role = roleRepository.findByEnumRole(EnumRole.USER);
        User user = User.builder()
                .registrationDate(LocalDate.now())
                .security(userSecurity)
                .role(role)
                .build();

        VerifyEmail verifyEmail = createVerifyEmail(user);

        verifyEmailRepository.save(verifyEmail);
        user.setVerifyEmail(verifyEmail);


        return user;
    }


    @Override
    public Optional<UserSecurity> findByEmail(String email) {
        return userSecurityRepository.findByEmail(email);
    }

    @Transactional
    @Override
    public boolean checkConfirmToken(TokenConfirmationDTO dto) {
        Optional<UserSecurity> byEmail = userSecurityRepository.findByEmail(dto.getEmail());
        LocalDateTime now = LocalDateTime.now();
        Date dateExpiryTime = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
        if (byEmail.isPresent()) {
            if (!byEmail.get().isEnabled()) {
                String token = byEmail.get().getUser().getVerifyEmail().getToken();
                if (dateExpiryTime.before(byEmail.get().getUser().getVerifyEmail().getExpiryTime())) {
                    if (token.equals(dto.getToken())) {
                        enableUserById(byEmail);
                        return true;
                    }
                }
            }
        }
        return false;
    }



    @Transactional
    public void enableUserById(Optional<UserSecurity> byEmail) {
        userSecurityRepository.enableUserById(byEmail.get().getId());
    }


    private VerifyEmail createVerifyEmail(User user) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tenMinutes = now.plusMinutes(10);
        Date dateExpiryTime = Date.from(tenMinutes.atZone(ZoneId.systemDefault()).toInstant());
        return VerifyEmail.builder()
                .user(user)
                .expiryTime(dateExpiryTime)
                .token(mailService.generateRandomToken())
                .user(user)
                .build();
    }




}
