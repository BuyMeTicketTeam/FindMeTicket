package com.booking.app.controller;

import com.booking.app.constant.CorsConfigConstants;
import com.booking.app.controller.api.LogoutAPI;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Arrays;
import java.util.Optional;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class LogoutController implements LogoutAPI {

    @CrossOrigin(allowCredentials = CorsConfigConstants.allowCredentials, origins = CorsConfigConstants.allowedOrigin, exposedHeaders = "Authorization")
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContext context = SecurityContextHolder.getContext();
        SecurityContextHolder.clearContext();
        context.setAuthentication(null);

        Optional<Cookie> refreshToken = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals("refreshToken"))
                .findAny();
        if (refreshToken.isPresent()) {
            ResponseCookie deletedCookie = ResponseCookie.from("refreshToken", null).build();
            response.setHeader(HttpHeaders.SET_COOKIE, deletedCookie.toString());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();

    }
}
