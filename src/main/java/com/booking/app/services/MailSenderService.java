package com.booking.app.services;

import com.booking.app.entity.UserSecurity;
import jakarta.mail.MessagingException;

import java.io.IOException;

public interface MailSenderService {
    void sendEmailRecoverPassword(String contextPath, String token, UserSecurity user);

    void sendEmailWithActivationToken(String token, UserSecurity user) throws IOException, MessagingException;
}
