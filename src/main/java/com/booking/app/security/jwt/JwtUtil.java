package com.booking.app.security.jwt;

import com.booking.app.entity.UserSecurity;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

@Component
@Log4j2
@Getter
public class JwtUtil {

    @Value("${jwtSecret}")
    private String jwtSecret;

    @Value("${accessTokenExpirationMs}")
    private int accessTokenExpirationMs;

    @Value("${refreshTokenExpirationMs}")
    private int refreshTokenExpirationMs;

    public boolean validateAccessToken(String accessToken, UserSecurity userSecurity) {
        String email = extractEmail(accessToken);
        return (email.equals(userSecurity.getEmail()) && validateToken(accessToken));
    }

    public boolean validateRefreshToken(String refreshToken, UserSecurity userSecurity) {
        String email = extractEmail(refreshToken);
        return (email.equals(userSecurity.getEmail()) && validateToken(refreshToken));
    }

    public String extractEmail(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(jwtSecret).build()
                    .parseClaimsJws(token)
                    .getBody();


        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
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
        Date expiryDate = new Date(now.getTime() + expirationMs * 1000L);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
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

    public String extractAccessTokenFromRequest(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer")) {
            return authorizationHeader.substring(7);
        }

        return null;
    }

    public String extractRefreshTokenFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refreshToken")) {
                    return cookie.getValue().substring(13);
                }
            }
        }
        return null;
    }
}



