package com.booking.app.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Component
@RequiredArgsConstructor
public class JWTDecoder implements JwtDecoder {

    private final JWTUtil jwtUtil;

    @Override
    public Jwt decode(String token) throws JwtException {
        SecretKey secretKey = new SecretKeySpec(jwtUtil.getJwtSecret().getBytes(), "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(secretKey).build().decode(token);
    }
}