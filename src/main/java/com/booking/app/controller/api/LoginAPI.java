package com.booking.app.controller.api;

import com.booking.app.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

@Validated
public interface LoginAPI {

    @Operation(summary = "Authentication User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User has been authenticated"),
            @ApiResponse(responseCode = "403", description = "Invalid credentials or such user doesn't exist")
    })
     ResponseEntity<?> login(@RequestBody @Valid @NotNull LoginDTO dto , HttpServletRequest request, HttpServletResponse response);

}
