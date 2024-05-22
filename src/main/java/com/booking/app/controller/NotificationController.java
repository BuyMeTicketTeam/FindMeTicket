package com.booking.app.controller;

import com.booking.app.entity.UserCredentials;
import com.booking.app.services.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Log4j2
@Tag(name = "Notification", description = "Notifications management")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/notifications/disable")
    @PreAuthorize("#{hasAnyRole('USER', 'ADMIN')}")
    @Operation(summary = "Disable")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Off"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<?> disableNotification(@AuthenticationPrincipal UserCredentials userCredentials) {
        notificationService.disable(userCredentials.getUser());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/notifications/enable")
    @PreAuthorize("#{hasAnyRole('USER', 'ADMIN')}")
    @Operation(summary = "Enable")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "On"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<?> enableNotification(@AuthenticationPrincipal UserCredentials userCredentials) {
        notificationService.enable(userCredentials.getUser());
        return ResponseEntity.ok().build();
    }

}
