package com.booking.app.security.jwt;

import com.booking.app.entity.UserSecurity;
import com.booking.app.entity.enums.EnumRole;
import com.booking.app.services.UserSecurityService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {
    @Value("${accessTokenValidTimeInMinutes}")
    private Integer accessTokenValidTimeInMinutes;

    @Value("${refreshTokenValidTimeInMinutes}")
    private Integer refreshTokenValidTimeInMinutes;

    @Value("${tokenKey}")
    private String tokenKey;

    private UserSecurityService userService;

    @PostConstruct
    void init() {
        tokenKey = Base64.getEncoder().encodeToString(tokenKey.getBytes());
    }

    /**
     * Method for creating access token.
     *
     * @param email this is email of user.
     * @param role  this is role of user.
     */
    public String createAccessToken(String email, EnumRole role) {
        Claims claims = Jwts.claims().setSubject(email).build();
        claims.put("roles", Collections.singleton(role.name()));
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.MINUTE, accessTokenValidTimeInMinutes);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(calendar.getTime())
                .signWith(SignatureAlgorithm.HS256, tokenKey)
                .compact();
    }

    /**
     * Method for creating refresh token.
     *
     * @param email this is email of user.
     */
    public String createRefreshToken(String email) {
        Claims claims = Jwts.claims().setSubject(email).build();
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.MINUTE, refreshTokenValidTimeInMinutes);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(calendar.getTime())
                .signWith(SignatureAlgorithm.HS256, tokenKey)
                .compact();
    }

    /**
     * Method that check if token still valid.
     *
     * @param token this is token.
     * @return {@link Boolean}
     */
    public boolean isTokenValid(String token) {
        boolean isValid = false;
        try {
            Jwts.parser().setSigningKey(tokenKey).build().parseClaimsJwt(token);
            isValid = true;
        } catch (Exception e) {
            //if can't parse token
        }
        return isValid;
    }

    /**
     * Method that get token by body request.
     *
     * @param servletRequest this is your request.
     * @return {@link String} of token or null.
     */
    public String getTokenByBody(HttpServletRequest servletRequest) {
        String token = servletRequest.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }

    /**
     * Method that create authentication.
     *
     * @param token token from request
     * @return {@link Authentication}
     */
    public Authentication getAuthentication(String token) {
        Optional<UserSecurity> optionalUser = userService.findByEmail(getEmailByToken(token));
        if (!optionalUser.isPresent()) {
            return null;
        }
        UserSecurity user = optionalUser.get();
        if (!user.isAccountNonLocked()) {
            return null;
        }
        return new UsernamePasswordAuthenticationToken(
                user.getEmail(), "", Collections.singleton(new SimpleGrantedAuthority(user.getUser().getRole().getEnumRole().name())));
    }

    /**
     * Method that get email from token.
     *
     * @param token token from request.
     * @return {@link String} of email.
     */
    public String getEmailByToken(String token) {
        return Jwts.parser().setSigningKey(tokenKey).build().parseClaimsJws(token).getBody().getSubject();
    }

}
