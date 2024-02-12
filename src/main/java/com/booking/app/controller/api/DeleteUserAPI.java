package com.booking.app.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface DeleteUserAPI {

    @Operation(summary = "Delete a user")
    @ApiResponse(responseCode = "200", description = "User and all of related info(cookies etc) has been deleted")
    ResponseEntity<?> deleteUser(HttpServletRequest request, HttpServletResponse response);

}
