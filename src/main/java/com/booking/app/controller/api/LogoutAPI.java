package com.booking.app.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface LogoutAPI {

    @Operation(summary = "Logout a user")
    @ApiResponse(responseCode = "200", description = "User has been logged out ")
    ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response);

}
