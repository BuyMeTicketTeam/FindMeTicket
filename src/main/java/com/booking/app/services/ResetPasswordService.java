package com.booking.app.services;

import com.booking.app.dto.RequestUpdatePasswordDTO;
import com.booking.app.dto.ResetPasswordDTO;
import com.booking.app.entity.UserCredentials;
import jakarta.mail.MessagingException;


public interface ResetPasswordService {

    /**
     * Sends a reset password code to the specified email.
     *
     * @param email    the email to which the reset code will be sent
     * @param language the language preference for the email content
     * @return true if the code was successfully sent, false otherwise
     * @throws MessagingException if there is an error while sending the email
     */
    boolean sendCode(String email, String language) throws MessagingException;

    /**
     * Resets the password for a user based on the provided reset token and new password.
     *
     * @param dto the data transfer object containing the reset token, email, and the new password
     * @return true if the password was successfully reset, false otherwise
     */
    boolean resetPassword(ResetPasswordDTO dto);

    /**
     * Changes the password for a given user if the provided last password matches the current password.
     *
     * @param updatePasswordDTO the data transfer object containing the last password and the new password
     * @param userCredentials   the user's credentials containing the current password
     * @return true if the password was successfully changed, false otherwise
     */
    boolean changePassword(RequestUpdatePasswordDTO updatePasswordDTO, UserCredentials userCredentials);
}
