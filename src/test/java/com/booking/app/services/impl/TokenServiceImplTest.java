package com.booking.app.services.impl;

import com.booking.app.entity.ConfirmToken;
import com.booking.app.entity.User;
import com.booking.app.entity.UserSecurity;
import com.booking.app.repositories.UserSecurityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;


import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class TokenServiceImplTest {

    @InjectMocks
    TokenServiceImpl tokenService;


    @Mock
    private UserSecurityRepository userSecurityRepository;

    @Test
    void testVerifyTokenSuccessCase() {
        String email = "javiermilei@gmail.com";
        String givenToken = "SAD88";

        Instant now = Instant.now();
        Instant later = now.plusSeconds(600);
        Date dateAfter10Minutes = Date.from(later);

        ConfirmToken token = ConfirmToken.builder().token("SAD88").expiryTime(dateAfter10Minutes).build();
        User user = User.builder().confirmToken(token).build();
        UserSecurity userSecurity = UserSecurity.builder().email("javiermilei@gmail.com").username("Javier Milei").user(user).build();

        Optional<UserSecurity> userByEmaail = Optional.of(userSecurity);

        when(userSecurityRepository.findByEmail(email)).thenReturn(userByEmaail);

        Assertions.assertTrue(tokenService.verifyToken(email, givenToken));
    }

    @Test
    void testVerifyTokenExpiredCase() {
        String email = "javiermilei@gmail.com";
        String givenToken = "SAD88";

        Instant now = Instant.now();
        Instant later = now.minusSeconds(600);
        Date dateAfter10Minutes = Date.from(later);

        ConfirmToken token = ConfirmToken.builder().token("SAD88").expiryTime(dateAfter10Minutes).build();
        User user = User.builder().confirmToken(token).build();
        UserSecurity userSecurity = UserSecurity.builder().email("javiermilei@gmail.com").username("Javier Milei").user(user).build();

        Optional<UserSecurity> userByEmaail = Optional.of(userSecurity);

        when(userSecurityRepository.findByEmail(email)).thenReturn(userByEmaail);

        Assertions.assertFalse(tokenService.verifyToken(email, givenToken));
    }

    @Test
    void testVerifyTokenWrongCase() {
        String email = "javiermilei@gmail.com";
        String givenToken = "HAPPY";

        Instant now = Instant.now();
        Instant later = now.minusSeconds(600);
        Date dateAfter10Minutes = Date.from(later);

        ConfirmToken token = ConfirmToken.builder().token("SAD88").expiryTime(dateAfter10Minutes).build();
        User user = User.builder().confirmToken(token).build();
        UserSecurity userSecurity = UserSecurity.builder().email("javiermilei@gmail.com").username("Javier Milei").user(user).build();

        Optional<UserSecurity> userByEmaail = Optional.of(userSecurity);

        when(userSecurityRepository.findByEmail(email)).thenReturn(userByEmaail);

        Assertions.assertFalse(tokenService.verifyToken(email, givenToken));
    }

    @Test
    void createConfirmToken() {

        User user = new User();

        TokenServiceImpl temp = Mockito.spy(tokenService);
        doReturn("SAD88").when(temp).generateRandomToken();

        user.setConfirmToken(temp.createConfirmToken(user));

        ConfirmToken token = user.getConfirmToken();

        assertNotNull(token);
        assertNotNull(token.getUser());
        assertNotNull(token.getExpiryTime());
        assertTrue((token.getExpiryTime().getTime()-new Date(System.currentTimeMillis()).getTime())/(60*1000)== 9);

    }

    @Test
    void generateRandomToken() {

        ReflectionTestUtils.setField(tokenService, "TOKEN_SYMBOLS", "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");

        String token = tokenService.generateRandomToken();

        assertNotNull(token);
        assertEquals(5, token.length());
    }
}