package com.booking.app.services.impl;

import com.booking.app.constants.ApiMessagesConstants;
import com.booking.app.dto.AuthenticatedUserDto;
import com.booking.app.dto.BasicLoginDto;
import com.booking.app.dto.SocialLoginDto;
import com.booking.app.entities.user.User;
import com.booking.app.security.filter.JwtProvider;
import com.booking.app.services.GoogleAccountService;
import com.booking.app.services.LoginService;
import com.booking.app.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import static com.booking.app.constants.AuthenticatedUserConstants.*;

@Service
@RequiredArgsConstructor
@Log4j2
public class LoginServiceImpl implements LoginService {

    private final AuthenticationManager authenticationManager;

    private final JwtProvider jwtProvider;

    private final GoogleAccountService googleAccountServiceImpl;

    @Override
    public AuthenticatedUserDto loginWithEmailAndPassword(BasicLoginDto dto, HttpServletResponse response) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        if (authentication.isAuthenticated()) {
            User user = (User) authentication.getPrincipal();
            handleRememberMe(dto, response);
            setHeadersAndCookies(response, user);
            return AuthenticatedUserDto.createBasicAuthorizedUser((user));
        } else {
            throw new BadCredentialsException(HttpStatus.UNAUTHORIZED.getReasonPhrase());
        }
    }

    @Override
    public AuthenticatedUserDto loginWithGoogle(SocialLoginDto dto, HttpServletRequest request, HttpServletResponse response) {
        return googleAccountServiceImpl.login(dto)
                .map(user -> {
                    setHeadersAndCookies(response, user);
                    return AuthenticatedUserDto.createGoogleAuthorizedUser(user);
                })
                .orElseGet(() -> {
                    log.warn(ApiMessagesConstants.INVALID_CLIENT_PROVIDER_ID_MESSAGE);
                    throw new BadCredentialsException(ApiMessagesConstants.INVALID_CLIENT_PROVIDER_ID_MESSAGE);
                });
    }

    /**
     * Generates and sets the access and refresh tokens in the response.
     *
     * @param response the HTTP response
     * @param user     the authenticated user
     */
    private void setHeadersAndCookies(HttpServletResponse response, User user) {
        String refreshToken = jwtProvider.generateRefreshToken(user.getEmail());
        String accessToken = jwtProvider.generateAccessToken(user.getEmail());

        CookieUtils.addCookie(response, REFRESH_TOKEN, refreshToken, jwtProvider.getRefreshTokenExpirationMs(), true, true);
        CookieUtils.addCookie(response, USER_ID, user.getId().toString(), jwtProvider.getRefreshTokenExpirationMs(), false, true);
        response.setHeader(HttpHeaders.AUTHORIZATION, BEARER + accessToken);
    }

    /**
     * Handles the "remember me" functionality by setting a corresponding cookie.
     *
     * @param basicLoginDTO the login data transfer object
     * @param response      the HTTP response
     */
    private void handleRememberMe(BasicLoginDto basicLoginDTO, HttpServletResponse response) {
        if (Boolean.TRUE.equals(basicLoginDTO.getRememberMe())) {
            CookieUtils.addCookie(response, REMEMBER_ME, basicLoginDTO.getRememberMe().toString(), jwtProvider.getRefreshTokenExpirationMs(), true, true);
        }
    }

}
