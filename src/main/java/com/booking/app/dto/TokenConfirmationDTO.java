package com.booking.app.dto;

import jakarta.validation.constraints.NotNull;

public class TokenConfirmationDTO {
    @NotNull
    private String token;
}
