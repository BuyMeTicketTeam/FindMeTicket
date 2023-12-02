package com.booking.app.security.jwt;

import com.booking.app.entity.UserSecurity;
import com.booking.app.security.jwt.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtUtilTest {

    @InjectMocks
    private JwtUtil jwtUtil;

    @Test
    void testAccessTokenGenerationAndValidation() {

        ReflectionTestUtils.setField(jwtUtil, "accessTokenExpirationMs", 360);
        ReflectionTestUtils.setField(jwtUtil, "jwtSecret", "urwehriaulwy38f32ydsfsrFSDF3RT423F43FWEDF342F324F34F3FG3GF");

        String email = "misha1234@gmail.com";
        String accessToken = jwtUtil.generateAccessToken(email);

        assertNotNull(accessToken);

        UserSecurity userSecurity = new UserSecurity();
        userSecurity.setEmail(email);

        assertTrue(jwtUtil.validateAccessToken(accessToken, userSecurity));


    }

    @Test
    void testRefreshTokenGenerationAndValidation() {

        ReflectionTestUtils.setField(jwtUtil, "refreshTokenExpirationMs", 720);
        ReflectionTestUtils.setField(jwtUtil, "jwtSecret", "urwehriaulwy38f32ydsfsrFSDF3RT423F43FWEDF342F324F34F3FG3GF");

        String email = "test@example.com";
        String refreshToken = jwtUtil.generateRefreshToken(email);

        assertNotNull(refreshToken);

        UserSecurity userSecurity = new UserSecurity();
        userSecurity.setEmail(email);

        assertTrue(jwtUtil.validateRefreshToken(refreshToken, userSecurity));


    }

    @Test
    void testTokenExtractionFromRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String accessToken = "mockedAccessToken";
        String refreshToken = "mockedRefreshToken";

        request.addHeader("Authorization", accessToken);
        request.addHeader("Refresh-Token", refreshToken);

        assertEquals(accessToken, jwtUtil.extractAccessTokenFromRequest(request));
        assertEquals(refreshToken, jwtUtil.extractRefreshTokenFromRequest(request));
    }
}
