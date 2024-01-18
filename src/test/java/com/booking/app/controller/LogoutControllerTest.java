package com.booking.app.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LogoutControllerTest {
    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private LogoutController logoutController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void logout_success() {
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        MockHttpServletResponse mockResponse = new MockHttpServletResponse();

        when(request.getCookies()).thenReturn(new Cookie[]{new Cookie("refreshToken", "mockRefreshToken"),
                new Cookie("USER_ID", "mockUserId"), new Cookie("rememberMe", "true")});

//        when(response.getCookies()).thenReturn(new Cookie[]{new Cookie("refreshToken", "mockRefreshToken"),
//                new Cookie("USER_ID", "mockUserId"), new Cookie("rememberMe", "true")});

        SecurityContextHolder.setContext(securityContext);

        ResponseEntity<?> responseEntity = logoutController.logout(mockRequest, mockResponse);

        assertEquals(200, responseEntity.getStatusCodeValue());

        verify(response, times(3)).addCookie(any(Cookie.class));
        verify(securityContext).setAuthentication(null);
       // verify(securityContext).clearContext();
    }


}