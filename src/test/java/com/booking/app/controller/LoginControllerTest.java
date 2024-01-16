package com.booking.app.controller;

import com.booking.app.dto.LoginDTO;
import com.booking.app.security.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;

import java.io.IOException;

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
    private JwtProvider jwtProvider;

    @Mock
    private Authentication authentication;

    @Mock
    private MockHttpServletRequest httpServletRequest;

    @Mock
    private MockHttpServletResponse httpServletResponse;

//    @Test
//    void testSuccessfulLogin() throws IOException {
//
//        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
//
//        when(authenticationManager.authenticate(any())).thenReturn(authentication);
//        when(authentication.isAuthenticated()).thenReturn(true);
//
//        when(jwtProvider.generateAccessToken(anyString())).thenReturn("mockedAccessToken");
//        when(jwtProvider.generateRefreshToken(anyString())).thenReturn("mockedRefreshToken");
//
//        LoginDTO loginDTO = LoginDTO.builder().email("mishaakamichael999@gmail.com").password("FutureDev999").build();
//
//        ResponseEntity<?> responseEntity = loginController.login(loginDTO, httpServletRequest, mockResponse);
//        httpServletRequest.addHeader("Authorization","mockedAccessToken");
//        httpServletRequest.addHeader("Refresh-Token","mockedRefreshToken");
//
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        verify(mockResponse).addHeader(eq("Authorization"), eq("mockedAccessToken"));
//        verify(mockResponse).addHeader(eq("Refresh-Token"), eq("mockedRefreshToken"));
//    }
//
//    @Test
//    void testFailLogin() {
//        when(authenticationManager.authenticate(any())).thenThrow(BadCredentialsException.class);
//
//        LoginDTO loginDTO = LoginDTO.builder().email("mishaakamichael999@gmail.com").password("FutureDev999").build();
//
//        assertThrows(BadCredentialsException.class, ()->loginController.login(loginDTO, httpServletRequest, httpServletResponse));
//    }

}