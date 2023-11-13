package com.booking.app.controller.api;


import com.booking.app.dto.*;
import com.booking.app.exception.exception.UserAlreadyExistAuthenticationException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.function.EntityResponse;

import java.io.IOException;


@Validated
public interface AuthAPI {


    @Operation(summary = "Authentication User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User has been authenticated"),
            @ApiResponse(responseCode = "400", description = "Invalid credentials or such user doesn't exist")
    })
    ResponseDTO<AuthResponseDTO> signIn(@RequestBody @NotNull @Valid LoginDTO dto);



}
