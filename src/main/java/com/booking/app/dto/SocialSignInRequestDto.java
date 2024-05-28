package com.booking.app.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SocialSignInRequestDto {
    @NotNull
    private String idToken;
}
