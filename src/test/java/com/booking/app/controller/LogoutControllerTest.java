package com.booking.app.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LogoutControllerTest {
    @InjectMocks
    private LogoutController logoutController;

    @Mock
    private SecurityContext securityContext;

    @Test
    public void testLogout() {
        SecurityContextHolder.setContext(securityContext);
        ResponseEntity<?> responseEntity = logoutController.logout();

        verify(securityContext).setAuthentication(null);
        verify(securityContext, atLeastOnce()).setAuthentication(null);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
}