package com.booking.app.controller.dto;

import com.booking.app.mapper.CustomDateDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.sql.Date;
import java.time.LocalDate;


@Data
@AllArgsConstructor
@Builder
public class UserDTO {

//    @NotBlank(message = "Invalid Login: Empty login")
//    @NotNull(message = "Invalid Login: Login is NULL")
//    @Size(min = 5, max = 30, message = "Invalid Login: Must be of 5 - 30 characters")
//    private final String username;

    @Pattern(regexp = "^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}$")
    private final String phoneNumber;


    @Valid
    private final RoleDTO role;




}
