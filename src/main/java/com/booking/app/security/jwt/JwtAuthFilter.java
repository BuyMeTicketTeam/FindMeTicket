package com.booking.app.security.jwt;

import com.booking.app.entity.UserCredentials;
import com.booking.app.util.CookieUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Log4j2
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String email = null;

        String accessTokenFromRequest = jwtProvider.extractAccessTokenFromRequest(request);
        String refreshTokenFromRequest = jwtProvider.extractRefreshTokenFromRequest(request);

        if (accessTokenFromRequest == null && refreshTokenFromRequest == null) {
            chain.doFilter(request, response);
            return;
        }

        email = jwtProvider.extractEmail(refreshTokenFromRequest);

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        if (accessTokenFromRequest != null && jwtProvider.validateAccessToken(accessTokenFromRequest, (UserCredentials) userDetails)) {
            String newAccessToken = jwtProvider.generateAccessToken(email);
            CookieUtils.addCookie(response, "refreshToken", refreshTokenFromRequest, 7200, true, true);
            response.setHeader("Authorization", String.format("%s %s", "Bearer", newAccessToken));
            response.setHeader("UserID", ((UserCredentials) userDetails).getId().toString());

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authenticationToken);
            SecurityContextHolder.setContext(context);

        } else if (refreshTokenFromRequest != null && jwtProvider.validateRefreshToken(refreshTokenFromRequest, (UserCredentials) userDetails)) {

            String newAccessToken = jwtProvider.generateAccessToken(email);
            String newRefreshToken = jwtProvider.generateRefreshToken(email);

            CookieUtils.addCookie(response, "refreshToken", newRefreshToken, 7200, true, true);
            response.setHeader("Authorization", String.format("%s %s", "Bearer", newAccessToken));
            response.setHeader("UserID", ((UserCredentials) userDetails).getId().toString());

            UserDetails refreshedUserDetails = userDetailsService.loadUserByUsername(email);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(refreshedUserDetails, refreshedUserDetails.getPassword(), refreshedUserDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authenticationToken);
            SecurityContextHolder.setContext(context);

        } else {
            SecurityContextHolder.clearContext();
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired tokens");
        }

        chain.doFilter(request, response);
    }

}
