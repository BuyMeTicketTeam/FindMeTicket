package com.booking.app.controller;

import com.booking.app.services.MailSenderService;
import com.booking.app.services.RegistrationService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RegistrationControllerTest {

    @InjectMocks
    private RegistrationController registrationController;

    @Mock
    private RegistrationService registrationService;

    @Mock
    private MailSenderService mailSenderService;

//    @Test
//    void testSuccessfulSignUp() throws MessagingException, IOException {
//        RegistrationDTO registrationDTO = RegistrationDTO.builder()
//                .email("mishaakamichael999@gmail.com")
//                .username("Michael123")
//                .password("qwerty123")
//                .confirmPassword("qwerty123").build();
//        String email = registrationDTO.getEmail();
//        EmailDTO emailDTO = EmailDTO.builder().email(email).build();
//        when(registrationService.register(registrationDTO)).thenReturn(emailDTO);
//        ResponseEntity<?> responseEntity = registrationController.signUp(registrationDTO);
//        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        Assertions.assertNotNull(responseEntity);
//    }
//
//    @Test
//    void testFailEmailExistsSignUp() throws MessagingException, IOException {
//        RegistrationDTO registrationDTO = RegistrationDTO.builder()
//                .email("mishaakamichael999@gmail.com")
//                .username("Michael123")
//                .password("qwerty123")
//                .confirmPassword("qwerty123").build();
//        when(registrationService.register(registrationDTO)).thenThrow(EmailAlreadyExistsException.class);
//        Assertions.assertThrows(EmailAlreadyExistsException.class, () -> registrationController.signUp(registrationDTO));
//    }

//    @Test
//    void testFailUsernameExistsSignUp() throws MessagingException, IOException {
//        RegistrationDTO registrationDTO = RegistrationDTO.builder()
//                .email("mishaakamichael999@gmail.com")
//                .username("Michael123")
//                .password("qwerty123")
//                .confirmPassword("qwerty123").build();
//        when(registrationService.register(registrationDTO)).thenThrow(UsernameAlreadyExistsException.class);
//        Assertions.assertThrows(UsernameAlreadyExistsException.class, () -> registrationController.signUp(registrationDTO));
//    }

//    @Test
//    void testSuccessfulConfirmEmailTokenVerification() {
//        TokenConfirmationDTO tokenDTO = TokenConfirmationDTO.builder()
//                .email("mishaakamichael999@gmail.com")
//                .token("SDG23").build();
//        when(registrationService.confirmCode(tokenDTO)).thenReturn(true);
//        ResponseEntity<?> responseEntity = registrationController.confirmEmailToken(tokenDTO);
//        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        Assertions.assertNotNull(responseEntity);
//    }
//
//    @Test
//    void testFailConfirmEmailTokenVerification() {
//        TokenConfirmationDTO tokenDTO = TokenConfirmationDTO.builder()
//                .email("mishaakamichael999@gmail.com")
//                .token("SDG23").build();
//        when(registrationService.confirmCode(tokenDTO)).thenReturn(false);
//        ResponseEntity<?> responseEntity = registrationController.confirmEmailToken(tokenDTO);
//        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
//        Assertions.assertNotNull(responseEntity);
//    }

//    @Test
//    void testSuccessfulResendConfirmEmailToken() throws IOException, MessagingException {
//        TokenConfirmationDTO tokenDTO = TokenConfirmationDTO.builder()
//                .email("mishaakamichael999@gmail.com")
//                .token("SDG23").build();
//        String email = tokenDTO.getEmail();
//        when(mailSenderService.resendEmail(email)).thenReturn(true);
//        ResponseEntity<?> responseEntity = registrationController.resendConfirmEmailToken(tokenDTO);
//        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        Assertions.assertNotNull(responseEntity);
//    }
//
//    @Test
//    void testFailResendConfirmEmailToken() throws IOException, MessagingException {
//        TokenConfirmationDTO tokenDTO = TokenConfirmationDTO.builder()
//                .email("mishaakamichael999@gmail.com")
//                .token("SDG23").build();
//        String email = tokenDTO.getEmail();
//        when(mailSenderService.resendEmail(email)).thenReturn(false);
//        ResponseEntity<?> responseEntity = registrationController.resendConfirmEmailToken(tokenDTO);
//        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
//        Assertions.assertNotNull(responseEntity);
//    }

}