package com.booking.app.dto.users;

import com.booking.app.annotations.PasswordMatches;
import com.booking.app.utils.ConfirmPasswordUtils;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@PasswordMatches
@NoArgsConstructor
public class RegistrationDTO implements ConfirmPasswordUtils {

    @NotBlank(message = "Empty username")
    @Size(min = 5, max = 30, message = "Username must be between 8 and 30 characters")
    private String username;

    @NotBlank(message = "Empty email")
    @Email(message = "This is not a valid email format")
    private String email;

    @NotBlank(message = "Empty password")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,30}$", message = "Password must be of 8 - 30 characters and contain at least one letter and one number")
    private String password;

    @NotBlank(message = "Empty confirm password")
    private String confirmPassword;

    @NotNull(message = "Boolean notification is required")
    private Boolean notification;

}


