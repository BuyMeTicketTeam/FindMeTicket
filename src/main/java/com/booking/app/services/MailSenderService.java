package com.booking.app.services;

import com.booking.app.entity.UserCredentials;
import com.booking.app.exception.exception.UserCredentialsNotFoundException;


public interface MailSenderService {

    /**
     * Sends an email with a confirmation token to the user.
     *
     * @param language        the language preference for the email content
     * @param subject         the subject of the email
     * @param token           the confirmation token to be included in the email
     * @param userCredentials the user credentials containing the recipient's email and username
     */
    void sendEmail(String language, String subject, String token, UserCredentials userCredentials);

    /**
     * Resends a confirmation email with a new confirmation token to the user.
     *
     * @param language the language preference for the email content
     * @param email    the email address of the user to resend the confirmation to
     * @throws UserCredentialsNotFoundException if no user with the given email is found
     */
    void resendEmail(String language, String email) throws UserCredentialsNotFoundException;

}
