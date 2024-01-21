package com.booking.app.controller;

import com.booking.app.controller.api.LogoutAPI;
import com.booking.app.util.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.booking.app.constant.CustomHttpHeaders.HEADER_REMEMBER_ME;
import static com.booking.app.constant.CustomHttpHeaders.HEADER_USER_ID;
import static com.booking.app.constant.JwtTokenConstants.REFRESH_TOKEN;

/**
 * Controller handling user logout functionality.
 * Implements the LogoutAPI interface.
 */
@RestController
@RequestMapping
@RequiredArgsConstructor
public class LogoutController implements LogoutAPI {

    /**
     * Handles HTTP POST request to '/logout'.
     * Clears the security context and invalidates the user's authentication.
     *
     * @return ResponseEntity with an HTTP status representing successful logout
     */
    @Override
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        CookieUtils.deleteCookie(request,response, REFRESH_TOKEN);
        CookieUtils.deleteCookie(request,response, HEADER_USER_ID);
        CookieUtils.deleteCookie(request,response, HEADER_REMEMBER_ME);
        SecurityContext context = SecurityContextHolder.getContext();
        SecurityContextHolder.clearContext();
        context.setAuthentication(null);

        return ResponseEntity.ok().build();
    }

}
