package com.booking.app.controller;

import com.booking.app.controller.api.LoginAPI;
import com.booking.app.dto.*;
import com.booking.app.security.jwt.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

/**
 * AuthController handles authentication-related operations.
 * This controller provides endpoints for user authentication.
 */
@RestController
@RequestMapping
@AllArgsConstructor
public class LoginController implements LoginAPI {

    private final UserDetailsService loginService;

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    /**
     * Handles user sign-in request.
     *
     * @param loginDTO The LoginDTO containing user credentials.
     * @param request  The HttpServletRequest containing the request information.
     * @param response The HttpServletResponse containing the response information.
     * @return ResponseEntity indicating the success of the sign-in operation.
     */
    @CrossOrigin(allowCredentials = "true", origins = "http://build")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO, HttpServletRequest request, HttpServletResponse response) {
        try {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword());

            Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

            if (authentication.isAuthenticated()) {
                String refreshToken = jwtUtil.generateRefreshToken(loginDTO.getEmail());
                String accessToken = jwtUtil.generateAccessToken(loginDTO.getEmail());
//
//                Cookie refreshTokenCookie = new Cookie("refreshToken",refreshToken);
//                refreshTokenCookie.setMaxAge(jwtUtil.getRefreshTokenExpirationMs());
//                refreshTokenCookie.setHttpOnly(true);
//                refreshTokenCookie.setSecure(true);
//                refreshTokenCookie.setPath("/login");

//                        httpOnly.(true)
//                        .secure(true)
//                        .path("/")
//                        .build();
                ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken)
                        .maxAge(jwtUtil.getRefreshTokenExpirationMs())
                        .httpOnly(true)
                        .secure(true)
                        .path("/login")
                        .sameSite("none")
                        .build();
              // response.addCookie(refreshTokenCookie);

                response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
                response.addHeader("Authorization", accessToken);

                return ResponseEntity.ok().build();
            }
        } catch (AuthenticationException e) {
            // Handle authentication failure
          //  return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.status(403).build();
    }

    @PostMapping("/get1")
    public ResponseEntity<?> get(@RequestBody LoginDTO loginDTO, HttpServletRequest request, HttpServletResponse response) {
        return ResponseEntity.ok().build();

    }
}


