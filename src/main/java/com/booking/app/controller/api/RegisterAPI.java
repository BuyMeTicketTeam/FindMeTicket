package com.booking.app.controller.api;

import com.booking.app.dto.*;
import com.booking.app.exception.exception.UserAlreadyExistAuthenticationException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import java.io.IOException;

@Validated
public interface RegisterAPI {

    @Operation(summary = "Register User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "409", description = "We’re sorry. This email already exists")
    })
    ResponseDTO<EmailDTO> signUp(@Valid @NotNull RegistrationDTO dto) throws UserAlreadyExistAuthenticationException, MessagingException, IOException;

    @Operation(summary = "Email confirmation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email is confirmed"),
            @ApiResponse(responseCode = "400", description = "Token from email is not right")
    })
    ResponseEntity<?> confirmEmailToken(@RequestBody @NotNull @Valid TokenConfirmationDTO dto);

    @Operation(summary = "Resend email confirmation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email is confirmed"),
    })
    ResponseEntity<?> resendConfirmEmailToken(@NotNull @Valid TokenConfirmationDTO dto) throws MessagingException, IOException;
}
