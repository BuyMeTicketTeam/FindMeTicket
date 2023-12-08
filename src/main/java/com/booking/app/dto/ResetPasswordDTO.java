package com.booking.app.dto;

import com.booking.app.annotation.PasswordMatches;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@PasswordMatches
public class ResetPasswordDTO implements ConfirmPasswordUtil {

    @Email
    private String email;
    @NotNull
    private String token;
    @NotNull
    private String password;
    @NotNull
    private String confirmPassword;

}