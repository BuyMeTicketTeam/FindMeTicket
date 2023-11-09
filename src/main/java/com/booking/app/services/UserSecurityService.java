package com.booking.app.services;

import com.booking.app.dto.LoginDTO;
import com.booking.app.dto.RegistrationDTO;
import com.booking.app.entity.UserSecurity;
import com.booking.app.exception.exception.UserAlreadyExistAuthenticationException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Validated
public interface UserSecurityService {
    @Transactional
    LoginDTO register(RegistrationDTO user) throws UserAlreadyExistAuthenticationException;


    Optional<UserSecurity> findByEmail(String email);

}
