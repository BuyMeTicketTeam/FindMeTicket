package com.booking.app.controller.api;

import com.booking.app.entity.UserCredentials;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

public interface SearchHistoryApi {

    @Operation(summary = "Searching history for user", description = "find search history by user id")
    @ApiResponse(responseCode = "200", description = "Returns history")
    @ApiResponse(responseCode = "404", description = "History not found")
    ResponseEntity<?> getHistory(@AuthenticationPrincipal UserCredentials userCredentials, HttpServletRequest request);
}
