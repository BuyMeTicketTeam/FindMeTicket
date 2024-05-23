package com.booking.app.services;

import com.booking.app.entity.UserCredentials;
import jakarta.mail.MessagingException;

/**
 * Service interface for sending email.
 */
public interface MailSenderService {

    /**
     * Sends an email with the specified HTML content, subject, and token to the specified user.
     *
     * @param htmlPage the HTML content of the email
     * @param subject  the subject of the email
     * @param token    the token to be included in the email
     * @param user     the user to whom the email will be sent
     * @throws MessagingException if there is an error sending the email
     */
    void sendEmail(String htmlPage, String subject, String token, UserCredentials user) throws MessagingException;

    /**
     * Sends an email with the specified HTML content, subject, and token to the specified user.
     *
     * @param htmlPage the HTML content of the email
     * @param email    the address to be sent to
     */
    boolean resendEmail(String email, String htmlPage);

}
