package com.booking.app.services;

import com.booking.app.dto.RequestUpdatePasswordDTO;
import com.booking.app.dto.PasswordDto;
import com.booking.app.entity.UserCredentials;
import com.booking.app.exception.exception.LastPasswordIsNotRightException;


public interface PasswordService {

    /**
     * Sends a reset code to the given email address.
     * If the user is found and their account is enabled, a new confirmation code is created and emailed.
     *
     * @param email    the email address to send the reset code to
     * @param language the language preference for the email content
     */
    void sendResetCode(String email, String language);

    /**
     * Resets the password for the user with the given email, if the provided reset code is valid.
     *
     * @param dto the data transfer object containing the email, reset code, and new password
     */
    void resetPassword(PasswordDto dto);

    /**
     * Changes the password for the given user credentials, if the provided current password is correct.
     *
     * @param dto             the data transfer object containing the current and new passwords
     * @param userCredentials the user credentials for which the password is to be changed
     * @throws LastPasswordIsNotRightException if the provided current password does not match the user's current password
     */
    void changePassword(RequestUpdatePasswordDTO dto, UserCredentials userCredentials) throws LastPasswordIsNotRightException;

}
