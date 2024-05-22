package com.booking.app.controller;

import com.booking.app.util.CookieUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Log out", description = "Log out authenticated user")
public class LogoutController {

    /**
     * Handles HTTP GET request to '/logout'
     *
     * @return ResponseEntity with an HTTP status representing successful logout
     */
    @GetMapping("/logout")
    @Operation(summary = "Log out a user")
    @ApiResponse(responseCode = "200", description = "Successfully logged")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        CookieUtils.deleteCookie(request, response, REFRESH_TOKEN);
        CookieUtils.deleteCookie(request, response, USER_ID);
        CookieUtils.deleteCookie(request, response, REMEMBER_ME);
        return ResponseEntity.ok().build();
    }

}
