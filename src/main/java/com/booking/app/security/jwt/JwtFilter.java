package com.booking.app.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtFilter extends GenericFilterBean {
    private JwtUtil jwtUtil;

    /**
     * Constructor.
     *
     * @param jwtUtil {@link JwtUtil} - tool for JWT
     */
    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * Method that check if request has token in body, if this token still valid, and set
     * authentication for spring.
     *
     * @param request  this is servlet that take request
     * @param response this is response servlet
     * @param chain     this is filter of chain
     */
    @Override
    public void doFilter(jakarta.servlet.ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = jwtUtil.getTokenByBody((HttpServletRequest) request);
        if (token != null && jwtUtil.isTokenValid(token)) {
            Authentication authentication = jwtUtil.getAuthentication(token);
            if (authentication != null) {
                log.info("User successfully authenticate - {}", authentication.getPrincipal());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }
        chain.doFilter(request, response);
    }
}
