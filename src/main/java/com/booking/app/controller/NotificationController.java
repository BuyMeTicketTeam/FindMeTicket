package com.booking.app.controller;

import com.booking.app.controller.api.NotificationApi;
import com.booking.app.entity.UserCredentials;
import com.booking.app.services.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Log4j2
public class NotificationController implements NotificationApi {

    private final NotificationService notificationService;

    @GetMapping("/notifications/disable")
    @PreAuthorize("#{hasAnyRole('USER', 'ADMIN')}")
    @Override
    public ResponseEntity<?> disableNotification(Authentication authentication) {
        if (authentication.getPrincipal() instanceof UserCredentials userCredentials) {
            notificationService.disable(userCredentials.getUser());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/notifications/enable")
    @PreAuthorize("#{hasAnyRole('USER', 'ADMIN')}")
    @Override
    public ResponseEntity<?> enableNotification(Authentication authentication) {
        if (authentication.getPrincipal() instanceof UserCredentials userCredentials) {
            notificationService.enable(userCredentials.getUser());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
