package com.booking.app.security.jwt;

import com.booking.app.entity.UserSecurity;
import com.booking.app.util.CookieUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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

        if (jwtProvider.validateAccessToken(accessTokenFromRequest, (UserSecurity) userDetails)) {

            CookieUtils.addCookie(response, "refresh-token", refreshTokenFromRequest, 7200, true, true);

            response.setHeader("Authorization", "Bearer " +accessTokenFromRequest);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } else if (jwtProvider.validateRefreshToken(refreshTokenFromRequest, (UserSecurity) userDetails)) {

            String newAccessToken = jwtProvider.generateAccessToken(email);
            String newRefreshToken = jwtProvider.generateAccessToken(email);

            CookieUtils.addCookie(response, "refresh-token", newRefreshToken, 7200, true, true);
            response.setHeader("Authorization", "Bearer " +newAccessToken);

            UserDetails refreshedUserDetails = userDetailsService.loadUserByUsername(email);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(refreshedUserDetails, refreshedUserDetails.getPassword(), refreshedUserDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } else {
            SecurityContextHolder.clearContext();
        }

        chain.doFilter(request, response);
    }

}
