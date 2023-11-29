package com.booking.app.security.jwt;

import com.booking.app.entity.UserSecurity;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

    private final JwtUtil jwtUtil;

    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String email = null;

        String accessTokenFromRequest = jwtUtil.extractAccessTokenFromRequest(request);
        String refreshTokenFromRequest = jwtUtil.extractRefreshTokenFromRequest(request);

        if (accessTokenFromRequest == null && refreshTokenFromRequest == null) {
            chain.doFilter(request, response);
            return;
        }

        email = jwtUtil.extractEmail(refreshTokenFromRequest);

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        if (jwtUtil.validateAccessToken(accessTokenFromRequest, (UserSecurity) userDetails)) {

            response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenFromRequest.toString());
            response.addHeader("Authorization", accessTokenFromRequest);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        else if (jwtUtil.validateRefreshToken(refreshTokenFromRequest, (UserSecurity) userDetails)) {

            String newAccessToken = jwtUtil.generateAccessToken(email);
            String newRefreshToken = jwtUtil.generateAccessToken(email);

            ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", newRefreshToken)
                    .maxAge(jwtUtil.getRefreshTokenExpirationMs())
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
            response.addHeader("Authorization", newAccessToken);

            UserDetails refreshedUserDetails = userDetailsService.loadUserByUsername(email);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(refreshedUserDetails, refreshedUserDetails.getPassword(), refreshedUserDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        else {
            SecurityContextHolder.clearContext();
        }

        chain.doFilter(request, response);
    }
}
