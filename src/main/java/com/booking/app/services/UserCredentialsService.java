package com.booking.app.services;

import com.booking.app.dto.RegistrationDTO;
import com.booking.app.entity.UserCredentials;
import com.booking.app.exception.exception.UserIsDisabledException;

import java.util.Optional;
import java.util.UUID;

public interface UserCredentialsService {

    /**
     * Finds user credentials by email.
     *
     * @param email The email address of the user.
     * @return An Optional containing user credentials if found, else empty.
     */
    Optional<UserCredentials> findByEmail(String email);

    /**
     * Saves user credentials.
     *
     * @param userCredentials The user credentials to be saved.
     * @return The saved user credentials.
     */
    UserCredentials save(UserCredentials userCredentials);

    /**
     * Checks if user credentials are enabled.
     *
     * @param userCredentials The user credentials to be checked.
     * @return True if user credentials are enabled, otherwise throws UserIsDisabledException.
     * @throws UserIsDisabledException If user credentials are disabled.
     */
    boolean isEnabled(UserCredentials userCredentials) throws UserIsDisabledException;

    /**
     * Updates the password for a user.
     *
     * @param uuid            The UUID of the user.
     * @param encodedPassword The encoded password to be updated.
     */
    void updatePassword(UUID uuid, String encodedPassword);

    /**
     * Enables user credentials.
     *
     * @param userCredentials The user credentials to be enabled.
     */
    void enableUserCredentials(UserCredentials userCredentials);

    /**
     * Creates user credentials based on registration DTO.
     *
     * @param dto The registration DTO containing user information.
     * @return The created user credentials.
     */
    UserCredentials createUserCredentials(RegistrationDTO dto);

    /**
     * Updates user credentials based on registration DTO.
     *
     * @param userCredentials The user credentials to be updated.
     * @param dto             The registration DTO containing updated information.
     * @return The updated user credentials.
     */
    UserCredentials updateUserCredentials(UserCredentials userCredentials, RegistrationDTO dto);

}
