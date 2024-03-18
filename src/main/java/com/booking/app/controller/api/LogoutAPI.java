package com.booking.app.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
@Tag(name = "Logging out", description = "Endpoints for logging out authenticated user")
public interface LogoutAPI {

    @Operation(summary = "Logout a user")
    @ApiResponse(responseCode = "200", description = "User has been logged out ")
    ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response);

}
