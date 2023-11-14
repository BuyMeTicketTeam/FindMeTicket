package com.booking.app.controller;


import com.booking.app.controller.api.AuthAPI;
import com.booking.app.dto.*;
import com.booking.app.exception.exception.UserAlreadyExistAuthenticationException;
import com.booking.app.services.UserSecurityService;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.EntityResponse;

import java.io.IOException;

@RestController
@RequestMapping
@AllArgsConstructor
public class AuthController implements AuthAPI {

    private final UserSecurityService service;

    @PostMapping("/login")
    @Override
    public ResponseEntity<?> signIn(LoginDTO dto) {
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
