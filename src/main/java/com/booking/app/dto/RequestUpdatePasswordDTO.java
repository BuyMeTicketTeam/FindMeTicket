package com.booking.app.dto;

import com.booking.app.annotation.PasswordMatches;
import com.booking.app.util.ConfirmPasswordUtil;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@PasswordMatches
public class RequestUpdatePasswordDTO implements ConfirmPasswordUtil {

    @NotNull
    private String lastPassword;

    @NotBlank(message = "Empty password")
    @NotNull(message = "Password is NULL")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,30}$", message = "Password must be of 8 - 30 characters and contain at least one letter and one number")
    private String password;

    private String confirmPassword;

}
