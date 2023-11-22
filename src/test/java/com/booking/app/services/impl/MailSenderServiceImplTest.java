package com.booking.app.services.impl;

import com.booking.app.repositories.UserSecurityRepository;
import com.booking.app.repositories.VerifyEmailRepository;
import com.booking.app.services.TokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.TemplateEngine;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MailSenderServiceImplTest {

    @InjectMocks
    private MailSenderServiceImpl mailSenderService;

    @Mock
    private  JavaMailSender mailSender;

    @Mock
    private  TemplateEngine templateEngine;

    @Mock
    private  UserSecurityRepository userSecurityRepository;

    @Mock
    private VerifyEmailRepository verifyEmailRepository;

    @Mock
    private  TokenService tokenService;




    @Test
    void sendEmail() {
    }

    @Test
    void resendEmail() {
    }
}