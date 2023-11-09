package com.booking.app.dto;


import com.booking.app.annotation.PasswordMatches;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
@PasswordMatches
public class RegistrationDTO {

    @NotBlank(message = "Invalid Login: Empty login")
    @NotNull(message = "Invalid Login: Login is NULL")
    @Size(min = 5, max = 30, message = "Invalid Login: Must be of 5 - 30 characters")
    private final String username;

    @NotNull(message = "Email is NULL")
    @Email(message = "This is not a valid email format")
    private final String email;

    @NotBlank(message = "Empty password")
    @NotNull(message = "Password is NULL")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,15}$", message = "Password must be of 8 - 15 characters and contain at least one letter and one number")
    private final String password;

    private final String confirmPassword;

    @Valid
    private final UserDTO user;


}


