package com.booking.app.controller;

import com.booking.app.services.PasswordService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PasswordControllerTest {

    @Mock
    private PasswordService passwordService;

    @InjectMocks
    private PasswordController passwordController;

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

//    @Test
//    void testSuccessfullyResetPassword() {
//        ResetPasswordDTO resetDTO = ResetPasswordDTO.builder()
//                .password("Sfsdf324")
//                .confirmPassword("Sfsdf324")
//                .code("FGQ12").build();
//        when(passwordService.resetPassword(resetDTO)).thenReturn(true);
//        ResponseEntity<?> responseEntity = passwordController.confirmResetPassword(resetDTO);
//        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        Assertions.assertNotNull(responseEntity);
//    }
//
//    @Test
//    void testFailResetPassword() {
//        ResetPasswordDTO resetDTO = ResetPasswordDTO.builder()
//                .password("Sfsdf324")
//                .confirmPassword("Sfsdf324")
//                .code("FGQ12").build();
//        when(passwordService.resetPassword(resetDTO)).thenReturn(false);
//        ResponseEntity<?> responseEntity = passwordController.confirmResetPassword(resetDTO);
//        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
//        Assertions.assertNotNull(responseEntity);
//    }

}