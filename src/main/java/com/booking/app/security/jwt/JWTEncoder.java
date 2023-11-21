package com.booking.app.security.jwt;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;


import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class JWTEncoder implements JwtEncoder {

    private final JWTUtil jwtUtil;

    @Override
    public Jwt encode(JwtEncoderParameters parameters) throws JwtEncodingException {
        SecretKey secretKeykey = new SecretKeySpec(jwtUtil.getJwtSecret().getBytes(), "HmacSHA256");
        JWKSource<SecurityContext> immutableSecret = new ImmutableSecret<SecurityContext>(secretKeykey);
        return new NimbusJwtEncoder(immutableSecret).encode(parameters);
    }
}
