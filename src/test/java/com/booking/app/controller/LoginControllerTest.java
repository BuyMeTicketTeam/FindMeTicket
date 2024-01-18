package com.booking.app.controller;

import com.booking.app.constant.CorsConfigConstants;
import com.booking.app.dto.LoginDTO;
import com.booking.app.entity.UserCredentials;
import com.booking.app.security.jwt.JwtProvider;
import com.booking.app.services.GoogleAccountService;
import com.booking.app.services.impl.GoogleAccountServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.http.entity.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.control.MappingControl;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

@ExtendWith(MockitoExtension.class)
class LoginControllerTest {
    @InjectMocks
    private LoginController loginController;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtProvider jwtProvider;
    @Mock
    private GoogleAccountServiceImpl googleAccountService;

//    @Mock
//    private Authentication authentication;

//    @Mock
//    private MockHttpServletRequest httpServletRequest;
//
//    @Mock
//    private MockHttpServletResponse httpServletResponse;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = standaloneSetup(loginController).build();
    }
// test validation DTO

    @Test
    void login_right_credentials_success() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        LoginDTO loginDTO = LoginDTO.builder().email("mishaakamichael999@gmail.com")
                .password("michael999")
                .rememberMe(true)
                .build();
        UserCredentials userCredentials = UserCredentials.builder().id(UUID.randomUUID()).build();
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);

        doReturn(true).when(authentication).isAuthenticated();

        when(authentication.getPrincipal()).thenReturn(userCredentials);
        when(jwtProvider.generateRefreshToken(Mockito.anyString())).thenReturn("mockedRefreshToken");
        when(jwtProvider.generateAccessToken(Mockito.anyString())).thenReturn("mockedAccessToken");

        mockMvc.perform(post("/login")
                        .content(objectMapper.writeValueAsString(loginDTO))
                        .contentType(MediaType.APPLICATION_JSON)

                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().exists(CorsConfigConstants.EXPOSED_HEADER_USER_ID))
                .andExpect(MockMvcResultMatchers.header().exists(CorsConfigConstants.EXPOSED_HEADER_REMEMBER_ME))
                .andExpect(MockMvcResultMatchers.header().exists(HttpHeaders.AUTHORIZATION))
                .andExpect(MockMvcResultMatchers.cookie().exists("refreshToken"));


    }

    @Test
    void login_invalidCredentials_unauthorized() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        LoginDTO loginDTO = LoginDTO.builder()
                .email("invalid@example.com")
                .password("invalidPassword")
                .rememberMe(false)
                .build();

        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        mockMvc.perform(post("/login")
                        .content(objectMapper.writeValueAsString(loginDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.header().doesNotExist(CorsConfigConstants.EXPOSED_HEADER_USER_ID))
                .andExpect(MockMvcResultMatchers.header().doesNotExist(CorsConfigConstants.EXPOSED_HEADER_REMEMBER_ME))
                .andExpect(MockMvcResultMatchers.header().doesNotExist(HttpHeaders.AUTHORIZATION))
                .andExpect(MockMvcResultMatchers.cookie().doesNotExist("refreshToken"));
    }

}