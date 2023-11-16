package com.booking.app.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class TokenConfirmationDTO {

    @NotNull
    private String email;
    private String token;
}
