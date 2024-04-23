package com.booking.app.controller.api;

import com.booking.app.dto.RegistrationDTO;
import com.booking.app.dto.TokenConfirmationDTO;
import com.booking.app.exception.exception.EmailAlreadyExistsException;
import com.booking.app.exception.exception.UsernameAlreadyExistsException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;

@Validated
@Tag(name = "Registering user", description = "Endpoints for registration and confirmation")
public interface RegisterAPI {

    @Operation(summary = "Register User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registered successfully"),
            @ApiResponse(responseCode = "409", description = "We’re sorry. This email already exists"),
            @ApiResponse(responseCode = "418", description = "We’re sorry. This username already exists")
    })
    ResponseEntity<?> signUp(String siteLanguage, @Valid @NotNull RegistrationDTO dto) throws EmailAlreadyExistsException, MessagingException, IOException, UsernameAlreadyExistsException;

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
    ResponseEntity<?> resendConfirmEmailToken(String siteLanguage, @NotNull @Valid TokenConfirmationDTO dto) throws MessagingException, IOException;

}
