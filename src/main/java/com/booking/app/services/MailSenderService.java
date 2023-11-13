package com.booking.app.services;

import com.booking.app.entity.UserSecurity;
import jakarta.mail.MessagingException;

import java.io.IOException;

public interface MailSenderService {
    void sendEmailRecoverPassword(String token, UserSecurity user);

    public void sendEmail(String user) throws IOException, MessagingException;
    void sendEmailWithActivationToken(String token, UserSecurity user) throws IOException, MessagingException;
    public String generateRandomToken();
}
