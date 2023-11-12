package com.booking.app.services.impl;

import com.booking.app.annotation.UserNotConfirmedException;
import com.booking.app.dto.LoginDTO;
import com.booking.app.dto.RegistrationDTO;
import com.booking.app.entity.Role;
import com.booking.app.entity.User;
import com.booking.app.entity.UserSecurity;
import com.booking.app.entity.VerifyEmail;
import com.booking.app.entity.enums.EnumRole;
import com.booking.app.exception.exception.UserAlreadyExistAuthenticationException;
import com.booking.app.mapper.UserMapper;
import com.booking.app.repositories.RoleRepository;
import com.booking.app.repositories.UserDataRepository;
import com.booking.app.repositories.UserSecurityRepository;
import com.booking.app.repositories.VerifyEmailRepository;
import com.booking.app.services.MailSenderService;
import com.booking.app.services.UserSecurityService;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.joda.time.DateTime;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.io.IOException;
import java.security.SecureRandom;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserSecurityServiceImpl implements UserDetailsService, UserSecurityService {

    private final UserSecurityRepository userSecurityRepository;
    private final RoleRepository roleRepository;
    private final UserDataRepository userDataRepository;
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
    public LoginDTO register(RegistrationDTO securityDTO) throws UserAlreadyExistAuthenticationException, MessagingException, IOException {
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

        userDataRepository.save(user);
        sendEmail(securityEntity);



        return mapper.toDtoLogin(securityEntity);

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

    private VerifyEmail createVerifyEmail(User user) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiryTime = now.plusMinutes(10); // Adjust the number of minutes as needed

        Date dateExpiryTime = Date.from(expiryTime.atZone(ZoneId.systemDefault()).toInstant());

        return VerifyEmail.builder()
                .user(user)
                .expiryTime(dateExpiryTime)
                .token(generateRandomToken())
                .user(user)
                .build();
    }


    private String generateRandomToken() {
        final String ALLOWED_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        final SecureRandom random = new SecureRandom();
        StringBuilder stringBuilder = new StringBuilder(5);
        for (int i = 0; i < 5; i++) {
            int randomIndex = random.nextInt(ALLOWED_CHARACTERS.length());
            char randomChar = ALLOWED_CHARACTERS.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }
        return stringBuilder.toString();
    }


    public void sendEmail(UserSecurity user) throws IOException, MessagingException {
        mailService.sendEmailWithActivationToken(user.getUser().getVerifyEmail().getToken(), user);
    }


}
