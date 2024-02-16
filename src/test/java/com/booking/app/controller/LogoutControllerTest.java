package com.booking.app.controller;

import com.booking.app.util.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.booking.app.constant.CustomHttpHeaders.REMEMBER_ME;
import static com.booking.app.constant.CustomHttpHeaders.USER_ID;
import static com.booking.app.constant.JwtTokenConstants.REFRESH_TOKEN;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@ExtendWith(MockitoExtension.class)
class LogoutControllerTest {
    @InjectMocks
    private LogoutController logoutController;

    @Mock
    private MockHttpServletRequest request;

    @Mock
    private MockHttpServletResponse response;

    @Mock
    private SecurityContext mockSecurityContext;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = standaloneSetup(logoutController).build();
        SecurityContextHolder.setContext(mockSecurityContext);
    }


    @Test
    void logout_clearContext_ok() throws Exception {
        try (MockedStatic<CookieUtils> dummy = mockStatic(CookieUtils.class)) {
            dummy.when(() -> CookieUtils.deleteCookie(request, response, REFRESH_TOKEN)).thenAnswer((Answer<Void>) invocation -> null);
            dummy.when(() -> CookieUtils.deleteCookie(request, response, USER_ID)).thenAnswer((Answer<Void>) invocation -> null);
            dummy.when(() -> CookieUtils.deleteCookie(request, response, REMEMBER_ME)).thenAnswer((Answer<Void>) invocation -> null);


            mockMvc.perform(post("/logout")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk());

            dummy.verify(() -> CookieUtils.deleteCookie(any(HttpServletRequest.class), any(HttpServletResponse.class), eq(REFRESH_TOKEN)));
            dummy.verify(() -> CookieUtils.deleteCookie(any(HttpServletRequest.class), any(HttpServletResponse.class), eq(USER_ID)));
            dummy.verify(() -> CookieUtils.deleteCookie(any(HttpServletRequest.class), any(HttpServletResponse.class), eq(REMEMBER_ME)));
            verify(mockSecurityContext, Mockito.times(1)).setAuthentication(null);
            assertThat(mockSecurityContext.getAuthentication()).isNull();
        }
    }

}
