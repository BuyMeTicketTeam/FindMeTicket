package com.booking.app.controller;

import com.booking.app.util.CookieUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockCookie;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.booking.app.constant.CustomHttpHeaders.REMEMBER_ME;
import static com.booking.app.constant.CustomHttpHeaders.USER_ID;
import static com.booking.app.constant.JwtTokenConstants.REFRESH_TOKEN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@ExtendWith(MockitoExtension.class)
class LogoutControllerTest {
    @InjectMocks
    private LogoutController logoutController;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private CookieUtils cookieUtils;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private MockCookie mockCookie;


    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = standaloneSetup(logoutController).build();
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


    @Test
    void logout_shouldDeleteCookiesAndClearSecurityContext() throws Exception {
//        when(request.getCookies()).thenReturn(new Cookie[]{new Cookie(REFRESH_TOKEN, "mockRefreshToken"),
//                new Cookie(USER_ID, "mockUserId"), new Cookie(REMEMBER_ME, "true")});
        // doNothing().when(response).setHeader(Mockito.anyString(), Mockito.anyString());

//        try (MockedStatic<CookieUtils> theMock = mockStatic(CookieUtils.class)) {
//            theMock.when(() -> CookieUtils.addCookie(eq(response), any(String.class), any(String.class), eq(0), eq(true), eq(true)))
//                    .thenAnswer(invocation -> {
//                        HttpServletResponse resp = invocation.getArgument(0);
//                        String name = invocation.getArgument(1);
//                        // Handle the logic as needed for the mock
//                        return null; // Or whatever you need to return
//                    });
            mockMvc.perform(MockMvcRequestBuilders.post("/logout")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());


//            verify(cookieUtils).deleteCookie(request, response, REFRESH_TOKEN);
//            verify(cookieUtils).deleteCookie(request, response, USER_ID);
//            verify(cookieUtils).deleteCookie(request, response, REMEMBER_ME);
        }


    }
