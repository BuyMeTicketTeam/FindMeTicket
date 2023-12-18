package com.booking.app.security.jwt;

import com.booking.app.entity.UserSecurity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtUserProviderTest {

    @InjectMocks
    private JwtProvider jwtProvider;

    @Test
    void testAccessTokenGenerationAndValidation() {

        ReflectionTestUtils.setField(jwtProvider, "accessTokenExpirationMs", 360);
        ReflectionTestUtils.setField(jwtProvider, "jwtSecret", "urwehriaulwy38f32ydsfsrFSDF3RT423F43FWEDF342F324F34F3FG3GF");

        String email = "misha1234@gmail.com";
        String accessToken = jwtProvider.generateAccessToken(email);

        assertNotNull(accessToken);

        UserSecurity userSecurity = new UserSecurity();
        userSecurity.setEmail(email);

        assertTrue(jwtProvider.validateAccessToken(accessToken, userSecurity));
    }

    @Test
    void testRefreshTokenGenerationAndValidation() {

        ReflectionTestUtils.setField(jwtProvider, "refreshTokenExpirationMs", 720);
        ReflectionTestUtils.setField(jwtProvider, "jwtSecret", "urwehriaulwy38f32ydsfsrFSDF3RT423F43FWEDF342F324F34F3FG3GF");

        String email = "test@example.com";
        String refreshToken = jwtProvider.generateRefreshToken(email);

        assertNotNull(refreshToken);

        UserSecurity userSecurity = new UserSecurity();
        userSecurity.setEmail(email);

        assertTrue(jwtProvider.validateRefreshToken(refreshToken, userSecurity));
    }

    @Test
    void testTokenExtractionFromRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String accessToken = "mockedAccessToken";
        String refreshToken = "mockedRefreshToken";

        request.addHeader("Authorization", accessToken);
        request.addHeader("Refresh-Token", refreshToken);

        assertEquals(accessToken, jwtProvider.extractAccessTokenFromRequest(request));
        assertEquals(refreshToken, jwtProvider.extractRefreshTokenFromRequest(request));
    }
    
}
