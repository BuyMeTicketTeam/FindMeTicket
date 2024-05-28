package com.booking.app.services.impl;

import com.booking.app.dto.AuthorizedUserDTO;
import com.booking.app.dto.LoginDTO;
import com.booking.app.dto.SocialSignInRequestDto;
import com.booking.app.entity.User;
import com.booking.app.security.filter.JwtProvider;
import com.booking.app.services.GoogleAccountService;
import com.booking.app.services.LoginService;
import com.booking.app.util.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import static com.booking.app.constant.CustomHttpHeaders.REMEMBER_ME;
import static com.booking.app.constant.CustomHttpHeaders.USER_ID;
import static com.booking.app.constant.JwtTokenConstants.BEARER;
import static com.booking.app.constant.JwtTokenConstants.REFRESH_TOKEN;
import static com.booking.app.controller.LoginController.CLIENT_ID_IS_NOT_RIGHT_MESSAGE;

@Service
@RequiredArgsConstructor
@Log4j2
public class LoginServiceImpl implements LoginService {

    private final AuthenticationManager authenticationManager;

    private final JwtProvider jwtProvider;

    private final GoogleAccountService googleAccountServiceImpl;

    @Override
    public ResponseEntity<?> loginWithEmailAndPassword(LoginDTO loginDTO, HttpServletRequest request, HttpServletResponse response) {
        if (isAlreadyLoggedIn(request))
            return ResponseEntity.ok().build();

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        User user = (User) authentication.getPrincipal();

        if (isAuthenticated(loginDTO, response, authentication, user))
            return ResponseEntity.ok()
                    .body(AuthorizedUserDTO.createBasicAuthorizedUser((user)));

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @Override
    public ResponseEntity<?> loginWithGoogle(SocialSignInRequestDto tokenDTO, HttpServletRequest request, HttpServletResponse response) {
        if (isAlreadyLoggedIn(request))
            return ResponseEntity.ok().build();
        AtomicReference<AuthorizedUserDTO> authorizedUserDTO = new AtomicReference<>();
        googleAccountServiceImpl.loginOAuthGoogle(tokenDTO)
                .ifPresentOrElse(
                        user -> {
                            generateAndSetTokens(response, user);
                            authorizedUserDTO.set(AuthorizedUserDTO.createGoogleAuthorizedUser(user));
                        },
                        () -> handleUserNotPresent(response)
                );
        return ResponseEntity.ok().body(authorizedUserDTO);
    }

    /**
     * Generates and sets the access and refresh tokens in the response.
     *
     * @param response        the HTTP response
     * @param user the authenticated user
     */
    private void generateAndSetTokens(HttpServletResponse response, User user) {
        String refreshToken = jwtProvider.generateRefreshToken(user.getEmail());
        String accessToken = jwtProvider.generateAccessToken(user.getEmail());

        CookieUtils.addCookie(response, REFRESH_TOKEN, refreshToken, jwtProvider.getRefreshTokenExpirationMs(), true, true);
        CookieUtils.addCookie(response, USER_ID, user.getId().toString(), jwtProvider.getRefreshTokenExpirationMs(), false, true);
        response.setHeader(HttpHeaders.AUTHORIZATION, BEARER + accessToken);
    }

    /**
     * Checks if the authentication is successful and sets the tokens if true.
     *
     * @param loginDTO        the login data transfer object
     * @param response        the HTTP response
     * @param authentication  the authentication object
     * @param user the authenticated user
     * @return true if authentication is successful, false otherwise
     */
    private boolean isAuthenticated(LoginDTO loginDTO, HttpServletResponse response, Authentication authentication, User user) {
        if (authentication.isAuthenticated()) {
            handleRememberMe(loginDTO, response);
            generateAndSetTokens(response, user);
            return true;
        }
        return false;
    }

    /**
     * Handles the "remember me" functionality by setting a corresponding cookie.
     *
     * @param loginDTO the login data transfer object
     * @param response the HTTP response
     */
    private void handleRememberMe(LoginDTO loginDTO, HttpServletResponse response) {
        if (Boolean.TRUE.equals(loginDTO.getRememberMe())) {
            CookieUtils.addCookie(response, REMEMBER_ME, loginDTO.getRememberMe().toString(), jwtProvider.getRefreshTokenExpirationMs(), true, true);
        }
    }

    /**
     * Sends an unauthorized status 401 response if the user is not present.
     *
     * @param response the HTTP response
     */
    private static void handleUserNotPresent(HttpServletResponse response) {
        try {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, CLIENT_ID_IS_NOT_RIGHT_MESSAGE);
        } catch (IOException e) {
            log.info(CLIENT_ID_IS_NOT_RIGHT_MESSAGE);
        }
    }

    /**
     * Checks if the user is already logged in by inspecting the request headers.
     *
     * @param request the HTTP request
     * @return true if the user is already logged in, false otherwise
     */
    private static boolean isAlreadyLoggedIn(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.SET_COOKIE) != null && request.getHeader(HttpHeaders.AUTHORIZATION) != null;
    }

}
