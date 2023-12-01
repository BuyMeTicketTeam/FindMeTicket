package com.booking.app.controller;

import com.booking.app.constant.CorsConfigConstants;
import com.booking.app.controller.api.LogoutAPI;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
@CrossOrigin(origins = CorsConfigConstants.allowedOrigin, maxAge = 3600,
        exposedHeaders = {CorsConfigConstants.exposedHeaderRefreshToken, CorsConfigConstants.exposedHeaderAuthorization })
public class LogoutController implements LogoutAPI {

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContext context = SecurityContextHolder.getContext();
        SecurityContextHolder.clearContext();
        context.setAuthentication(null);
        return ResponseEntity.ok().build();
    }

}
