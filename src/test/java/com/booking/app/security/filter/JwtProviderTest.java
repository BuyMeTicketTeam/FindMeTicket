package com.booking.app.security.filter;

import com.booking.app.constant.JwtTokenConstants;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class JwtProviderTest {

    @InjectMocks
    private JwtProvider jwtProvider;

    private MockHttpServletRequest request;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
    }

//    @Test
//    void testAccessTokenGenerationAndValidation() {
//        ReflectionTestUtils.setField(jwtProvider, "accessTokenExpirationMs", 360);
//        ReflectionTestUtils.setField(jwtProvider, "jwtSecret", "urwehriaulwy38f32ydsfsrFSDF3RT423F43FWEDF342F324F34F3FG3GF");
//
//        String email = "misha1234@gmail.com";
//        String accessToken = jwtProvider.generateAccessToken(email);
//
//        assertNotNull(accessToken);
//
//        UserCredentials userSecurity = new UserCredentials();
//        userSecurity.setEmail(email);
//
//        assertTrue(jwtProvider.validateAccessToken(accessToken, userSecurity));
//    }
//
//    @Test
//    void testRefreshTokenGenerationAndValidation() {
//        ReflectionTestUtils.setField(jwtProvider, "refreshTokenExpirationMs", 720);
//        ReflectionTestUtils.setField(jwtProvider, "jwtSecret", "urwehriaulwy38f32ydsfsrFSDF3RT423F43FWEDF342F324F34F3FG3GF");
//
//        String email = "test@example.com";
//        String refreshToken = jwtProvider.generateRefreshToken(email);
//
//        assertNotNull(refreshToken);
//
//        UserCredentials userSecurity = new UserCredentials();
//        userSecurity.setEmail(email);
//
//        assertTrue(jwtProvider.validateRefreshToken(refreshToken, userSecurity));
//    }

    @Test
    void testTokenExtractionFromRequest() {
        String accessToken = JwtTokenConstants.BEARER + "mockedAccessToken";
        String refreshToken = "mockedRefreshToken";

        request.addHeader(HttpHeaders.AUTHORIZATION, accessToken);
        Cookie cookie = new Cookie(JwtTokenConstants.REFRESH_TOKEN, refreshToken);
        request.setCookies(cookie);
        assertEquals("mockedAccessToken", JwtProvider.extractAccessToken(request));
        assertEquals(refreshToken, JwtProvider.extractRefreshToken(request));
    }

}
