package com.booking.app.security.filter;

import com.booking.app.constant.JwtTokenConstants;
import com.google.api.client.util.Strings;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
@Log4j2
@Getter
public class JwtProvider {

    @Value("${jwtSecret}")
    private String jwtSecret;

    @Value("${accessTokenExpirationMs}")
    private int accessTokenExpirationMs;

    @Value("${refreshTokenExpirationMs}")
    private int refreshTokenExpirationMs;

    public boolean validateAccessToken(String accessToken) {
        return validateToken(accessToken);
    }

    public boolean validateRefreshToken(String refreshToken) {
        return validateToken(refreshToken);
    }

    public String extractEmail(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(jwtSecret).build()
                    .parseClaimsJws(token)
                    .getBody();


        } catch (MalformedJwtException | ExpiredJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            log.error("Error parsing JWT token: {}", e.getMessage());
            return null;
        }
        return claims.getSubject();
    }


    public String generateAccessToken(String email) {
        return generateToken(email, accessTokenExpirationMs);
    }

    public String generateRefreshToken(String email) {
        return generateToken(email, refreshTokenExpirationMs);
    }

    private String generateToken(String email, int expirationMs) {
        Date now = new Date();
        long expirationMillis = expirationMs * 60L * 1000L;
        Date expiryDate = new Date(now.getTime() + expirationMillis);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key())
                .compact();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    private boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(key()).build()
                    .parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    public static String extractAccessToken(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (!Strings.isNullOrEmpty(token) && token.startsWith(JwtTokenConstants.BEARER.trim())) {
            return token.substring(7);
        }
        return null;
    }

    public static String extractRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(JwtTokenConstants.REFRESH_TOKEN)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

}
