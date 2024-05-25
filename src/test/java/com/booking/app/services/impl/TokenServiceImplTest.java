package com.booking.app.services.impl;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class TokenServiceImplTest {

//    @InjectMocks
//    TokenServiceImpl tokenService;
//
//
//    @Mock
//    private UserCredentialsRepository userCredentialsRepository;
//
//    @Test
//    void testVerifyTokenSuccessCase() {
//        String email = "javiermilei@gmail.com";
//        String givenToken = "SAD88";
//
//        Instant now = Instant.now();
//        Instant later = now.plusSeconds(600);
//        Date dateAfter10Minutes = Date.from(later);
//
//        ConfirmationCode token = ConfirmationCode.builder().token("SAD88").expiryTime(dateAfter10Minutes).build();
//        User user = User.builder().confirmationCode(token).build();
//        UserCredentials userCredentials = UserCredentials.builder().email("javiermilei@gmail.com").username("Javier Milei").user(user).build();
//
//        Optional<UserCredentials> userByEmaail = Optional.of(userCredentials);
//
//        when(userCredentialsRepository.findByEmail(email)).thenReturn(userByEmaail);
//
//        Assertions.assertTrue(tokenService.verifyToken(email, givenToken));
//    }
//
//    @Test
//    void testVerifyTokenExpiredCase() {
//        String email = "javiermilei@gmail.com";
//        String givenToken = "SAD88";
//
//        Instant now = Instant.now();
//        Instant later = now.minusSeconds(600);
//        Date dateAfter10Minutes = Date.from(later);
//
//        ConfirmationCode token = ConfirmationCode.builder().token("SAD88").expiryTime(dateAfter10Minutes).build();
//        User user = User.builder().confirmationCode(token).build();
//        UserCredentials userCredentials = UserCredentials.builder().email("javiermilei@gmail.com").username("Javier Milei").user(user).build();
//
//        Optional<UserCredentials> userByEmaail = Optional.of(userCredentials);
//
//        when(userCredentialsRepository.findByEmail(email)).thenReturn(userByEmaail);
//
//        Assertions.assertFalse(tokenService.verifyToken(email, givenToken));
//    }
//
//    @Test
//    void testVerifyTokenWrongCase() {
//        String email = "javiermilei@gmail.com";
//        String givenToken = "HAPPY";
//
//        Instant now = Instant.now();
//        Instant later = now.plusSeconds(600);
//        Date dateAfter10Minutes = Date.from(later);
//
//        ConfirmationCode token = ConfirmationCode.builder().token("SAD88").expiryTime(dateAfter10Minutes).build();
//        User user = User.builder().confirmationCode(token).build();
//        UserCredentials userCredentials = UserCredentials.builder().email("javiermilei@gmail.com").username("Javier Milei").user(user).build();
//
//        Optional<UserCredentials> userByEmaail = Optional.of(userCredentials);
//
//        when(userCredentialsRepository.findByEmail(email)).thenReturn(userByEmaail);
//
//        Assertions.assertFalse(tokenService.verifyToken(email, givenToken));
//    }

//    @Test
//    void testCreateConfirmToken() {
//
//        User user = new User();
//
//        TokenServiceImpl temp = Mockito.spy(tokenService);
//        doReturn("SAD88").when(temp).generateRandomToken();
//
//        user.setConfirmToken(temp.createConfirmToken(user));
//
//        ConfirmToken token = user.getConfirmToken();
//
//        assertNotNull(token);
//        assertNotNull(token.getUser());
//        assertNotNull(token.getExpiryTime());
//
//        long minutes = (token.getExpiryTime().getTime() - new Date(System.currentTimeMillis()).getTime()) / (60 * 1000);
//
//        assertTrue(minutes >= 9 && minutes <= 10);
//    }
//
//    @Test
//    void testGenerateRandomToken() {
//
//        ReflectionTestUtils.setField(tokenService, "TOKEN_SYMBOLS", "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
//
//        String token = tokenService.generateRandomToken();
//
//        assertNotNull(token);
//        assertEquals(5, token.length());
//    }

}