package com.booking.app.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class LoginDTO {


    @NotNull(message = "Email is NULL")
    @Email(message = "This is not a valid email format")
    private final String email;

    @NotBlank(message = "Empty password")
    @NotNull(message = "Password is NULL")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,30}$", message = "Password must be of 8 - 30 characters and contain at least one letter and one number")
    private final String password;


}


