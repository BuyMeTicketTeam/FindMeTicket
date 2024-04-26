package com.booking.app.services.impl;

import com.booking.app.dto.ResetPasswordDTO;
import com.booking.app.entity.ConfirmToken;
import com.booking.app.entity.User;
import com.booking.app.entity.UserCredentials;
import com.booking.app.repositories.UserCredentialsRepository;
import com.booking.app.repositories.VerifyEmailRepository;
import com.booking.app.services.MailSenderService;
import com.booking.app.services.TokenService;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResetPasswordServiceImplTest {

    @InjectMocks
    ResetPasswordServiceImpl resetPasswordService;

    @Mock
    private MailSenderService mailSenderService;

    @Mock
    private UserCredentialsRepository userCredentialsRepository;

    @Mock
    private VerifyEmailRepository verifyEmailRepository;

    @Mock
    private TokenService tokenService;

    @Mock
    private PasswordEncoder passwordEncoder;

//    @Test
//    void testSuccessfullySendEmailResetPassword() throws MessagingException {
//        String email = "dkfshkf@gmail.com";
//
//        Instant now = Instant.now();
//        Instant later = now.plusSeconds(600);
//        Date dateAfter10Minutes = Date.from(later);
//
//        ConfirmToken token = ConfirmToken.builder().token("SAD88").expiryTime(dateAfter10Minutes).build();
//
//        User user = User.builder().confirmToken(token).build();
//
//        UserCredentials userCredentials = UserCredentials.builder().enabled(true).user(user).build();
//
//        when(userCredentialsRepository.findByEmail(email)).thenReturn(Optional.of(userCredentials));
//        when(tokenService.createConfirmToken(user)).thenReturn(token);
//
//        assertTrue(resetPasswordService.hasEmailSent(email));
//    }

//    @Test
//    void testSendEmailResetPasswordNoSuchEmail() throws MessagingException {
//        String email = "dkfshkf@gmail.com";
//
//        when(userCredentialsRepository.findByEmail(email)).thenThrow(new UsernameNotFoundException("No such email"));
//
//        Assertions.assertThrows(UsernameNotFoundException.class, () -> resetPasswordService.hasEmailSent(email));
//    }
//
//    @Test
//    void testSendEmailResetPasswordAccountNotActivated() throws MessagingException {
//        String email = "dkfshkf@gmail.com";
//
//        Instant now = Instant.now();
//        Instant later = now.plusSeconds(600);
//        Date dateAfter10Minutes = Date.from(later);
//
//        ConfirmToken token = ConfirmToken.builder().token("SAD88").expiryTime(dateAfter10Minutes).build();
//
//        User user = User.builder().confirmToken(token).build();
//
//        UserCredentials userCredentials = UserCredentials.builder().enabled(false).user(user).build();
//
//        when(userCredentialsRepository.findByEmail(email)).thenReturn(Optional.of(userCredentials));
//
//        assertFalse(resetPasswordService.hasEmailSent(email));
//    }

    @Test
    void testSuccessfullyResetPassword() {
        ResetPasswordDTO resetPasswordDTO = ResetPasswordDTO.builder().email("dkfshkf@gmail.com")
                .token("SAD88").password("12345").confirmPassword("12345").build();

        Instant now = Instant.now();
        Instant later = now.plusSeconds(600);
        Date dateAfter10Minutes = Date.from(later);

        ConfirmToken token = ConfirmToken.builder().token("SAD88").expiryTime(dateAfter10Minutes).build();

        User user = User.builder().confirmToken(token).build();

        UserCredentials userCredentials = UserCredentials.builder().enabled(true).user(user).build();

        when(userCredentialsRepository.findByEmail(resetPasswordDTO.getEmail())).thenReturn(Optional.of(userCredentials));
        when(tokenService.verifyToken(resetPasswordDTO.getEmail(), resetPasswordDTO.getToken())).thenReturn(true);

        assertTrue(resetPasswordService.resetPassword(resetPasswordDTO));
    }

    @Test
    void testResetPasswordTokenNotMatches() {
        ResetPasswordDTO resetPasswordDTO = ResetPasswordDTO.builder().email("dkfshkf@gmail.com")
                .token("SAD88").password("12345").confirmPassword("12345").build();

        Instant now = Instant.now();
        Instant later = now.plusSeconds(600);
        Date dateAfter10Minutes = Date.from(later);

        ConfirmToken token = ConfirmToken.builder().token("SAD88").expiryTime(dateAfter10Minutes).build();

        User user = User.builder().confirmToken(token).build();

        UserCredentials userCredentials = UserCredentials.builder().enabled(true).user(user).build();

        when(userCredentialsRepository.findByEmail(resetPasswordDTO.getEmail())).thenReturn(Optional.of(userCredentials));
        when(tokenService.verifyToken(resetPasswordDTO.getEmail(), resetPasswordDTO.getToken())).thenReturn(false);

        assertFalse(resetPasswordService.resetPassword(resetPasswordDTO));
    }

    @Test
    void testResetPasswordNoSuchUser() {
        ResetPasswordDTO resetPasswordDTO = ResetPasswordDTO.builder().email("dkfshkf@gmail.com")
                .token("SAD88").password("12345").confirmPassword("12345").build();

        when(userCredentialsRepository.findByEmail(resetPasswordDTO.getEmail())).thenReturn(Optional.empty());

        assertFalse(resetPasswordService.resetPassword(resetPasswordDTO));
    }

    @Test
    void testResetPassword() {
        ResetPasswordDTO resetPasswordDTO = ResetPasswordDTO.builder().email("dkfshkf@gmail.com")
                .token("SAD88").password("12345").confirmPassword("12345").build();

        Instant now = Instant.now();
        Instant later = now.plusSeconds(600);
        Date dateAfter10Minutes = Date.from(later);

        ConfirmToken token = ConfirmToken.builder().token("SAD88").expiryTime(dateAfter10Minutes).build();

        User user = User.builder().confirmToken(token).build();

        UserCredentials userCredentials = UserCredentials.builder().enabled(false).user(user).build();

        when(userCredentialsRepository.findByEmail(resetPasswordDTO.getEmail())).thenReturn(Optional.of(userCredentials));

        assertFalse(resetPasswordService.resetPassword(resetPasswordDTO));
    }

}