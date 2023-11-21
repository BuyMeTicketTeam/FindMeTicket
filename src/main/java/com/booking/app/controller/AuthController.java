package com.booking.app.controller;

import com.booking.app.controller.api.AuthAPI;
import com.booking.app.dto.*;
import com.booking.app.services.UserSecurityService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.HTML;
import java.util.Arrays;

/**
 * AuthController handles authentication-related operations.
 * This controller provides endpoints for user authentication.
 */
@RestController
@RequestMapping
@AllArgsConstructor
@Data
public class AuthController implements AuthAPI {

    private final UserSecurityService service;


    /**
     * Handles user sign-in request.
     *
     * @param dto      The LoginDTO containing user credentials.
     * @param request  The HttpServletRequest containing the request information.
     * @param response The HttpServletResponse containing the response information.
     * @return ResponseEntity indicating the success of the sign-in operation.
     */
    @PostMapping("/login")
    @Override
    public ResponseEntity<?> signIn(@RequestBody LoginDTO dto , HttpServletRequest request, HttpServletResponse response) {
            return ResponseEntity.ok().build();
      }


    }

// Arrays.stream(cookies).forEach(cookie -> cookie.getAttribute("Authorization"));
//        Cookie cookie = new Cookie("refreshToken", refreshToken);
//        cookie.setHttpOnly(true);
//        cookie.setMaxAge((int) jwtUtil.getRefreshTokenExpiration() / 1000);
//        cookie.setPath("/");
//        response.addCookie(cookie);

//    @PostMapping("/test")
//    public ResponseEntity<?> test(HttpServletResponse response) {
//        response.addCookie();
//        response.setHeader();
//
//        Cookie cookie = new Cookie("jwt", "jwt");
//        cookie.setHttpOnly(true);
//        response.addCookie(cookie);
//        return ResponseEntity.status(HttpStatus.OK).build();
//
//    }

