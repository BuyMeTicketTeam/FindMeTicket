package com.booking.app.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("security.jwt")
@Setter
@Getter
public class JwtProps {
    private String secret;
    private Integer accessTokenExpirationMin;
    private Integer refreshTokenExpirationMin;
}
