package com.booking.app.controller;


import com.booking.app.controller.api.AuthAPI;
import com.booking.app.dto.*;
import com.booking.app.exception.exception.UserAlreadyExistAuthenticationException;
import com.booking.app.services.UserSecurityService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

import lombok.Data;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.EntityResponse;

import java.io.IOException;

@RestController
@RequestMapping
@AllArgsConstructor
@Data
public class AuthController implements AuthAPI {

    private final UserSecurityService service;

    @PostMapping("/login")
    @Override
    public ResponseEntity<?> signIn(LoginDTO dto) {
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @PostMapping("/test")
    public ResponseEntity<?> test(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt", "jwt");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return ResponseEntity.status(HttpStatus.OK).build();

    }


}
