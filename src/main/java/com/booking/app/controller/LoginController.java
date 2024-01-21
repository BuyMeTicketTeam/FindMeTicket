package com.booking.app.controller;

import com.booking.app.controller.api.LoginAPI;
import com.booking.app.dto.LoginDTO;
import com.booking.app.dto.OAuth2IdTokenDTO;
import com.booking.app.entity.UserCredentials;
import com.booking.app.security.jwt.JwtProvider;
import com.booking.app.services.impl.GoogleAccountServiceImpl;
import com.booking.app.util.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static com.booking.app.constant.CustomHttpHeaders.HEADER_REMEMBER_ME;
import static com.booking.app.constant.CustomHttpHeaders.HEADER_USER_ID;
import static com.booking.app.constant.JwtTokenConstants.BEARER;
import static com.booking.app.constant.JwtTokenConstants.REFRESH_TOKEN;

/**
 * LoginController handles authentication-related operations.
 * This controller provides endpoints for user authentication.
 */
@RestController
@RequestMapping
@RequiredArgsConstructor
@Log4j2
public class LoginController implements LoginAPI {

    private final AuthenticationManager authenticationManager;

    private final JwtProvider jwtProvider;

    private final GoogleAccountServiceImpl googleAccountServiceImpl;

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

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword());

        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        if (response.getHeader(HttpHeaders.SET_COOKIE) != null && response.getHeader(HttpHeaders.AUTHORIZATION) != null)
            return ResponseEntity.ok().build();

        if (authentication.isAuthenticated()) {
            if (loginDTO.getRememberMe()) {
                response.addHeader(HEADER_REMEMBER_ME, loginDTO.getRememberMe().toString());
            }

            UserCredentials userCredentials = (UserCredentials) authentication.getPrincipal();

            String refreshToken = jwtProvider.generateRefreshToken(loginDTO.getEmail());
            String accessToken = jwtProvider.generateAccessToken(loginDTO.getEmail());

            CookieUtils.addCookie(response, REFRESH_TOKEN, refreshToken, 7200, true, true);
            response.setHeader(HEADER_USER_ID, userCredentials.getId().toString());
            response.setHeader(HttpHeaders.AUTHORIZATION, BEARER + accessToken);

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authenticationToken);
            SecurityContextHolder.setContext(context);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(401).build();
    }

    @PostMapping("/oauth2/authorize/*")
    public ResponseEntity<?> login(@RequestBody OAuth2IdTokenDTO tokenDTO, HttpServletResponse response) throws GeneralSecurityException, IOException {
        UserCredentials userCredentials = googleAccountServiceImpl.loginOAuthGoogle(tokenDTO);

        String refreshToken = jwtProvider.generateRefreshToken(userCredentials.getEmail());
        String accessToken = jwtProvider.generateAccessToken(userCredentials.getEmail());

        CookieUtils.addCookie(response, REFRESH_TOKEN, refreshToken, jwtProvider.getRefreshTokenExpirationMs(), true, true);
        response.setHeader(HEADER_USER_ID, userCredentials.getId().toString());
        response.setHeader(HttpHeaders.AUTHORIZATION, BEARER + accessToken);

        return ResponseEntity.ok().build();
    }

}
