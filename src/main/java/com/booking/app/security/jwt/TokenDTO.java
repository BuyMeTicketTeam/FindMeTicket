package com.booking.app.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter@Getter@AllArgsConstructor@NoArgsConstructor
public class TokenDTO {
    private String userId;
    private String accessToken;
    private String refreshToken;
}
