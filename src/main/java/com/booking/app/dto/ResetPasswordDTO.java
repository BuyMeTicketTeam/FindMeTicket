package com.booking.app.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ResetPasswordDTO {
    @Email
    private String email;
    @NotNull
    private String token;
    @NotNull
    private String password;

}
