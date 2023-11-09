package com.booking.app.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtFilter extends GenericFilterBean {
    private JwtTokenTool tool;

    /**
     * Constructor.
     *
     * @param tool {@link JwtTokenTool} - tool for JWT
     */
    public JwtFilter(JwtTokenTool tool) {
        this.tool = tool;
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
        String token = tool.getTokenByBody((HttpServletRequest) request);
        if (token != null && tool.isTokenValid(token)) {
            Authentication authentication = tool.getAuthentication(token);
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
