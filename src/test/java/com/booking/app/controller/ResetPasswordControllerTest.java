package com.booking.app.controller;

import com.booking.app.dto.EmailDTO;
import com.booking.app.dto.ResetPasswordDTO;
import com.booking.app.services.ResetPasswordService;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ResetPasswordControllerTest {

    @Mock
    private ResetPasswordService resetPasswordService;

    @InjectMocks
    private ResetPasswordController resetPasswordController;

//    @Test
//    void testSuccessfullySendResetToken() throws MessagingException, IOException {
//        EmailDTO emailDTO = EmailDTO.builder()
//                .email("mishaakamichael999@gmail.com").build();
//        String email = emailDTO.getEmail();
//        when(resetPasswordService
//                .hasEmailSent(email)).thenReturn(true);
//        ResponseEntity<?> responseEntity = resetPasswordController.sendResetToken(emailDTO);
//        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        Assertions.assertNotNull(responseEntity);
//    }
//
//    @Test
//    void testFailSendResetToken() throws MessagingException, IOException {
//        EmailDTO emailDTO = EmailDTO.builder()
//                .email("mishaakamichael999@gmail.com").build();
//        String email = emailDTO.getEmail();
//        when(resetPasswordService.hasEmailSent(email)).thenReturn(false);
//        ResponseEntity<?> responseEntity = resetPasswordController.sendResetToken(emailDTO);
//        Assertions.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
//        Assertions.assertNotNull(responseEntity);
//    }

    @Test
    void testSuccessfullyResetPassword() {
        ResetPasswordDTO resetDTO = ResetPasswordDTO.builder()
                .password("Sfsdf324")
                .confirmPassword("Sfsdf324")
                .token("FGQ12").build();
        when(resetPasswordService.resetPassword(resetDTO)).thenReturn(true);
        ResponseEntity<?> responseEntity = resetPasswordController.confirmResetPassword(resetDTO);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertNotNull(responseEntity);
    }

    @Test
    void testFailResetPassword() {
        ResetPasswordDTO resetDTO = ResetPasswordDTO.builder()
                .password("Sfsdf324")
                .confirmPassword("Sfsdf324")
                .token("FGQ12").build();
        when(resetPasswordService.resetPassword(resetDTO)).thenReturn(false);
        ResponseEntity<?> responseEntity = resetPasswordController.confirmResetPassword(resetDTO);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        Assertions.assertNotNull(responseEntity);
    }

}