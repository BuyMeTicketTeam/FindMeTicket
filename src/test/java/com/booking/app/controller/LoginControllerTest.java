package com.booking.app.controller;

import com.booking.app.dto.LoginDTO;
import com.booking.app.security.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginControllerTest {
    @InjectMocks
    private LoginController loginController;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private Authentication authentication;

    @Mock
    private MockHttpServletRequest httpServletRequest;

    @Mock
    private MockHttpServletResponse httpServletResponse;

    @Test
    void testSuccessfulLogin() {

        HttpServletResponse mockResponse = mock(HttpServletResponse.class);

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);

        when(jwtUtil.generateAccessToken(anyString())).thenReturn("mockedAccessToken");
        when(jwtUtil.generateRefreshToken(anyString())).thenReturn("mockedRefreshToken");

        LoginDTO loginDTO = LoginDTO.builder().email("mishaakamichael999@gmail.com").password("FutureDev999").build();

        ResponseEntity<?> responseEntity = loginController.login(loginDTO, httpServletRequest, mockResponse);
        httpServletRequest.addHeader("Authorization","mockedAccessToken");
        httpServletRequest.addHeader("Refresh-Token","mockedRefreshToken");

        // Assertions for a successful login
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(mockResponse).addHeader(eq("Authorization"), eq("mockedAccessToken"));
        verify(mockResponse).addHeader(eq("Refresh-Token"), eq("mockedRefreshToken"));
//        assertEquals("mockedAccessToken", httpServletResponse.getHeader("Authorization"));
//        assertEquals("mockedRefreshToken", httpServletResponse.getHeader("Refresh-Token"));
        // Add more assertions if needed
    }

    @Test
    void testFailLogin() {
        // Mocking authentication manager behavior for failed login
        when(authenticationManager.authenticate(any())).thenThrow(BadCredentialsException.class);

        // Create a LoginDTO with invalid credentials
        LoginDTO loginDTO = LoginDTO.builder().email("mishaakamichael999@gmail.com").password("FutureDev999").build();

        assertThrows(BadCredentialsException.class, ()->loginController.login(loginDTO, httpServletRequest, httpServletResponse));

        // Perform the login action
//        ResponseEntity<?> responseEntity = loginController.login(loginDTO, httpServletRequest, httpServletResponse);

        // Assertions for a failed login
//        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
//        assertNull(httpServletResponse.getHeader("Authorization"));
//        assertNull(httpServletResponse.getHeader("Refresh-Token"));
    }
}