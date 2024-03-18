package com.booking.app.controller.api;

import com.booking.app.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Validated
@Tag(name = "Logging in",description = "Endpoints for basic and social login")
public interface LoginAPI {

    @Operation(summary = "Basic authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User has been authenticated"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials or such user doesn't exist")
    })
    ResponseEntity<?> login(@RequestBody @Valid @NotNull LoginDTO dto, HttpServletRequest request, HttpServletResponse response) throws IOException;

    @Operation(summary = "OAuth 2.0 authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User has been authenticated"),
            @ApiResponse(responseCode = "401", description = "ID token is invalid")
    })
    ResponseEntity<?> loginOAuth2(@RequestBody @Valid @NotNull OAuth2IdTokenDTO tokenDTO, HttpServletResponse response) throws java.io.IOException, GeneralSecurityException;

}
