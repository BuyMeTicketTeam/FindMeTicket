package com.booking.app.controller;

import com.booking.app.constant.CorsConfigConstants;
import com.booking.app.controller.api.LoginAPI;
import com.booking.app.dto.*;
import com.booking.app.util.CookieUtils;
import com.booking.app.security.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * LoginController handles authentication-related operations.
 * This controller provides endpoints for user authentication.
 */
@RestController
@RequestMapping
@RequiredArgsConstructor
@Log4j2
@CrossOrigin(origins = {CorsConfigConstants.ALLOWED_ORIGIN_80, CorsConfigConstants.ALLOWED_ORIGIN_81}, maxAge = 3600,
        exposedHeaders = {CorsConfigConstants.EXPOSED_HEADER_REFRESH_TOKEN, CorsConfigConstants.EXPOSED_HEADER_AUTHORIZATION}, allowCredentials = "true")
public class LoginController implements LoginAPI {

    private final AuthenticationManager authenticationManager;

    private final JwtProvider jwtProvider;

    /**
     * Handles user sign-in request.
     *
     * @param loginDTO The LoginDTO containing user credentials.
     * @param request  The HttpServletRequest containing the request information.
     * @param response The HttpServletResponse containing the response information.
     * @return ResponseEntity indicating the success of the sign-in operation.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO, HttpServletRequest request, HttpServletResponse response) throws IOException {

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword());

        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        if (authentication.isAuthenticated()) {
            String refreshToken = jwtProvider.generateRefreshToken(loginDTO.getEmail());
            String accessToken = jwtProvider.generateAccessToken(loginDTO.getEmail());

            response.setHeader("Authorization", "Bearer " + accessToken);
            CookieUtils.addCookie(response, "refresh-token", refreshToken, 7200, true, true);

            return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(403).build();
    }

}


