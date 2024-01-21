package com.booking.app.services;

import com.booking.app.dto.ResetPasswordDTO;
import jakarta.mail.MessagingException;

/**
 * Service interface for resetting passwords.
 */
public interface ResetPasswordService {

     /**
      * Generates a new token for resetting the password and sends an email to the specified recipient.
      *
      * @param email The email address of the recipient.
      * @return Returns true if the email with the reset password link was sent successfully; otherwise, returns false.
      * @throws MessagingException If there is an issue with sending the email.
      */
     boolean hasEmailSent(String email) throws MessagingException;

     /**
      * Resets the password to a new one using the information provided in the ResetPasswordDTO.
      *
      * @param dto The ResetPasswordDTO containing the email and the new password.
      * @return Returns true if the password was successfully changed; otherwise, returns false.
      */
     boolean resetPassword(ResetPasswordDTO dto);
}
