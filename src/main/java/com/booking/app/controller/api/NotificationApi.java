package com.booking.app.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

@Tag(name = "Notification", description = "On/off notifications")
public interface NotificationApi {

    @Operation(summary = "Disable")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Off"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    ResponseEntity<?> disableNotification(Authentication authentication);

    @Operation(summary = "Enable")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "On"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    ResponseEntity<?> enableNotification(Authentication authentication);
}
