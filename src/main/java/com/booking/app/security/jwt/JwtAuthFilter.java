package com.booking.app.security.jwt;

import com.booking.app.entity.UserCredentials;
import com.booking.app.util.CookieUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.booking.app.constant.CustomHttpHeaders.USER_ID;
import static com.booking.app.constant.JwtTokenConstants.BEARER;
import static com.booking.app.constant.JwtTokenConstants.REFRESH_TOKEN;

@Log4j2
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String accessToken = jwtProvider.extractAccessTokenFromRequest(request);
        String refreshToken = jwtProvider.extractRefreshTokenFromRequest(request);

        if (accessToken == null && refreshToken == null) {
            chain.doFilter(request, response);
            return;
        }

        String email = jwtProvider.extractEmail(refreshToken);
        if (email == null) {
            handleAuthenticationFailure(response);
            return;
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        if (accessToken != null && jwtProvider.validateAccessToken(accessToken, (UserCredentials) userDetails)) {
            String newAccessToken = jwtProvider.generateAccessToken(email);

            saveAuthenticationDataToResponse(response, newAccessToken, refreshToken, userDetails);
            setSecurityContext(userDetails);
        } else if (refreshToken != null && jwtProvider.validateRefreshToken(refreshToken, (UserCredentials) userDetails)) {
            String newAccessToken = jwtProvider.generateAccessToken(email);
            String newRefreshToken = jwtProvider.generateRefreshToken(email);

            saveAuthenticationDataToResponse(response, newAccessToken, newRefreshToken, userDetails);
            setSecurityContext(userDetails);
        } else {
            handleAuthenticationFailure(response);
            return;
        }

        chain.doFilter(request, response);
    }

    private void saveAuthenticationDataToResponse(HttpServletResponse response, String accessToken, String refreshToken, UserDetails userDetails) {
        CookieUtils.addCookie(response, REFRESH_TOKEN, refreshToken, jwtProvider.getRefreshTokenExpirationMs(), true, true);
        CookieUtils.addCookie(response, USER_ID, ((UserCredentials) userDetails).getId().toString(), jwtProvider.getRefreshTokenExpirationMs(), false, true);
        response.setHeader(HttpHeaders.AUTHORIZATION, BEARER + accessToken);
    }

    private void setSecurityContext(UserDetails userDetails) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authenticationToken);
        SecurityContextHolder.setContext(context);
    }

    private void handleAuthenticationFailure(HttpServletResponse response) throws IOException {
        SecurityContextHolder.clearContext();
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired tokens");
    }

}
