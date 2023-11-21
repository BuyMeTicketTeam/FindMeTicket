package com.booking.app.services;

import com.booking.app.entity.UserSecurity;
import jakarta.mail.MessagingException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.io.IOException;

/**
 * Service interface for sending email.
 */
public interface MailSenderService {

     /**
      * This method sending email with specified token.
      *
      * @param htmlPage String html representation of the letter
      * @param subject String subject of the letter
      * @param token String generated token that is needed to confirm email
      * @param user UserSecurity recipient
      * @throws MessagingException If there is an issue with sending the confirmation email.
      */
     void sendEmail(String htmlPage, String subject, String token, UserSecurity user) throws MessagingException;

     /**
      * This method generates new token end sends it
      * on specified email
      *
      * @param email String recipient
      * @throws UsernameNotFoundException If such user does not exist
      * @throws MessagingException If there is an issue with sending the confirmation email.
      */
     public void resendEmail(String email) throws UsernameNotFoundException, MessagingException;
}
