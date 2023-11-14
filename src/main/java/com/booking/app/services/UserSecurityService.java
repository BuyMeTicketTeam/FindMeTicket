package com.booking.app.services;

import com.booking.app.dto.EmailDTO;
import com.booking.app.dto.RegistrationDTO;
import com.booking.app.dto.TokenConfirmationDTO;
import com.booking.app.entity.UserSecurity;
import com.booking.app.exception.exception.UserAlreadyExistAuthenticationException;
import jakarta.mail.MessagingException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Validated
public interface UserSecurityService {

    EmailDTO register(RegistrationDTO user) throws UserAlreadyExistAuthenticationException, MessagingException, IOException;
    Optional<UserSecurity> findByEmail(String email);

    boolean enableIfValid(TokenConfirmationDTO dto);









}
