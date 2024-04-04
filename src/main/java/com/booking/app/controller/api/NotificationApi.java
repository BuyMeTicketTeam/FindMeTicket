package com.booking.app.controller.api;

import com.booking.app.entity.UserCredentials;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Notification", description = "On/off notifications")
public interface NotificationApi {

    @Operation(summary = "Disable")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Off"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    ResponseEntity<?> disableNotification(UserCredentials userCredentials);

    @Operation(summary = "Enable")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "On"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    ResponseEntity<?> enableNotification(UserCredentials userCredentials);
}
