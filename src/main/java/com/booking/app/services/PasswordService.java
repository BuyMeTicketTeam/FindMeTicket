package com.booking.app.services;

import com.booking.app.dto.users.PasswordDto;
import com.booking.app.dto.users.RequestUpdatePasswordDTO;
import com.booking.app.entities.user.User;
import com.booking.app.exceptions.badrequest.LastPasswordIsNotRightException;


public interface PasswordService {

    /**
     * Resets the password for the user with the given email, if the provided reset code is valid.
     *
     * @param dto the data transfer object containing the email, reset code, and new password
     */
    void resetPassword(PasswordDto dto);

    /**
     * Changes the password for the given user, if the provided current password is correct.
     *
     * @param dto  the data transfer object containing the current and new passwords
     * @param user the user for which the password is to be changed
     * @throws LastPasswordIsNotRightException if the provided current password does not match the user's current password
     */
    void changePassword(RequestUpdatePasswordDTO dto, User user) throws LastPasswordIsNotRightException;

}
