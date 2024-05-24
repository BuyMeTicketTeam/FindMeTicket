package com.booking.app.services;

import com.booking.app.entity.UserCredentials;
import com.booking.app.exception.exception.UserIsDisabledException;
import com.booking.app.exception.exception.UserIsNotFoundException;

import java.util.UUID;

public interface UserCredentialsService {

    /**
     * Finds a user by their email.
     *
     * @param email the email of the user to be found
     * @return the user credentials associated with the given email
     * @throws UserIsNotFoundException if no user with the given email is found
     */
    UserCredentials findByEmail(String email) throws UserIsNotFoundException;

    /**
     * Saves the user credentials.
     *
     * @param userCredentials the user credentials to be saved
     * @return the saved user credentials
     */
    UserCredentials save(UserCredentials userCredentials);

    /**
     * Checks if a user is enabled.
     *
     * @param userCredentials the user credentials to be checked
     * @return true if the user is enabled, otherwise throws UserIsDisabledException
     * @throws UserIsDisabledException if the user is not enabled
     */
    boolean isEnabled(UserCredentials userCredentials) throws UserIsDisabledException;

    /**
     * Updates the password for a user.
     *
     * @param uuid            the UUID of the user whose password is to be updated
     * @param encodedPassword the new encoded password
     */
    void updatePassword(UUID uuid, String encodedPassword);

}
