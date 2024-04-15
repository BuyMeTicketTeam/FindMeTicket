package com.booking.app.controller;

import com.booking.app.controller.api.LogoutAPI;
import com.booking.app.util.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.booking.app.constant.CustomHttpHeaders.REMEMBER_ME;
import static com.booking.app.constant.CustomHttpHeaders.USER_ID;
import static com.booking.app.constant.JwtTokenConstants.REFRESH_TOKEN;

/**
 * Controller handling user logout functionality.
 */
@RestController
@RequestMapping
@RequiredArgsConstructor
public class LogoutController implements LogoutAPI {

    /**
     * Handles HTTP GET request to '/logout'.
     * Clears the security context and invalidates the user's authentication.
     *
     * @return ResponseEntity with an HTTP status representing successful logout
     */
    @Override
    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        CookieUtils.deleteCookie(request, response, REFRESH_TOKEN);
        CookieUtils.deleteCookie(request, response, USER_ID);
        CookieUtils.deleteCookie(request, response, REMEMBER_ME);

        return ResponseEntity.ok().build();
    }

}
