package com.booking.app.services;

import com.booking.app.dto.RequestUpdatePasswordDTO;
import com.booking.app.dto.ResetPasswordDTO;
import com.booking.app.entity.UserCredentials;
import com.booking.app.exception.exception.PasswordIsNotRightException;
import jakarta.mail.MessagingException;


public interface PasswordService {

    /**
     * Sends a reset code to the user's email.
     *
     * @param email    the email of the user to send the reset code to
     * @param language the language preference for the email content
     * @throws MessagingException if there is an error while sending the email
     */
    void sendResetCode(String email, String language) throws MessagingException;

    /**
     * Resets the user's password if the provided reset code is valid.
     *
     * @param dto the DTO containing the email, reset code, and new password
     */
    void resetPassword(ResetPasswordDTO dto);

    /**
     * Changes the user's password if the provided current password matches the stored password.
     *
     * @param dto             the DTO containing the current password and the new password
     * @param userCredentials the user's credentials
     * @throws PasswordIsNotRightException if the current password does not match the stored password
     */
    void changePassword(RequestUpdatePasswordDTO dto, UserCredentials userCredentials) throws PasswordIsNotRightException;

}
