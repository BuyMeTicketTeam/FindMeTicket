package com.booking.app.dto;

import com.booking.app.annotation.PasswordMatches;
import com.booking.app.util.ConfirmPasswordUtil;
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
public class PasswordDto implements ConfirmPasswordUtil {

    @Email
    private String email;
    @NotNull
    private String code;
    @NotNull
    private String password;
    @NotNull
    private String confirmPassword;

}