package com.booking.app.services;

import com.booking.app.entity.UserSecurity;
import jakarta.mail.MessagingException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.io.IOException;

public interface MailSenderService {

     void sendEmail(String htmlPage, String subject, String token, UserSecurity user) throws MessagingException;

     public void resendEmail(String email) throws UsernameNotFoundException, MessagingException, IOException;
}
