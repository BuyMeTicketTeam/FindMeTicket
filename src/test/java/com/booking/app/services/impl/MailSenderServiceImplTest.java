package com.booking.app.services.impl;

import com.booking.app.repositories.UserCredentialsRepository;
import com.booking.app.repositories.ConfirmationCodeRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.TemplateEngine;

@ExtendWith(MockitoExtension.class)
class MailSenderServiceImplTest {

    @InjectMocks
    MailSenderServiceImpl mailSenderService;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private TemplateEngine templateEngine;

    @Mock
    private UserCredentialsRepository userCredentialsRepository;

    @Mock
    private ConfirmationCodeRepository confirmationCodeRepository;

//    @Mock
//    private TokenService tokenService;


//    @Test
//    void testResendEmailUserNotFound() throws MessagingException, UserPrincipalNotFoundException {
//
//        String email = "ewyrwey_sdds@gmail.com";
//
//        when(userCredentialsRepository.findByEmail(email)).thenReturn(Optional.empty());
//
//        assertFalse(mailSenderService.resendEmail(email));
//    }

//    @Test
//    void testSuccessfullyResendEmail() throws MessagingException, UserPrincipalNotFoundException {
//
//        MailSenderServiceImpl temp = Mockito.spy(mailSenderService);
//
//        String email = "ewyrwey_sdds@gmail.com";
//
//        Instant now = Instant.now();
//        Instant later = now.plusSeconds(600);
//        Date dateAfter10Minutes = Date.from(later);
//
//        ConfirmToken token = ConfirmToken.builder().token("SAD88").expiryTime(dateAfter10Minutes).build();
//        User user = User.builder().confirmToken(token).build();
//        UserCredentials userCredentials = UserCredentials.builder().email("ewyrwey_sdds@gmail.com").username("ewyrweysdds").user(user).build();
//
//        when(userCredentialsRepository.findByEmail(email)).thenReturn(Optional.of(userCredentials));
//        when(tokenService.createConfirmToken(user)).thenReturn(token);
//        doNothing().when(temp).sendEmail("confirmMailUa", "Email confirmation", token.getToken(), userCredentials);
//
//        assertTrue(temp.resendEmail(email));
//    }

}