package com.booking.app.services;

import com.booking.app.controller.dto.LoginDTO;
import com.booking.app.controller.dto.RegistrationDTO;
import com.booking.app.exceptionhandling.exception.UserAlreadyExistAuthenticationException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Validated
public interface UserSecurityService {
    @Transactional
    LoginDTO register(RegistrationDTO user) throws UserAlreadyExistAuthenticationException;


}
