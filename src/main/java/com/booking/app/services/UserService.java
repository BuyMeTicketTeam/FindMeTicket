package com.booking.app.services;

import com.booking.app.dto.RegistrationDTO;
import com.booking.app.entity.User;
import com.booking.app.exception.exception.UserIsDisabledException;

import java.util.Optional;
import java.util.UUID;

public interface UserService {

    /**
     * Updates the notification setting for a user.
     *
     * @param uuid         the ID of the user
     * @param notification the new notification setting
     */
    void updateNotification(UUID uuid, boolean notification);

    /**
     * Finds user by email.
     *
     * @param email The email address of the user.
     * @return An Optional containing user if found, else empty.
     */
    Optional<User> findByEmail(String email);

    /**
     * Saves user.
     *
     * @param user The user to be saved.
     * @return The saved user.
     */
    User save(User user);

    /**
     * Checks if user is enabled.
     *
     * @param user The user to be checked.
     * @return True if user are enabled, otherwise throws UserIsDisabledException.
     * @throws UserIsDisabledException If user is disabled.
     */
    boolean isEnabled(User user) throws UserIsDisabledException;

    /**
     * Updates the password for a user.
     *
     * @param uuid            The UUID of the user.
     * @param encodedPassword The encoded password to be updated.
     */
    void updatePassword(UUID uuid, String encodedPassword);

    /**
     * Enables user.
     *
     * @param user The user to be enabled.
     */
    void enableUser(User user);

    /**
     * Creates user based on registration DTO.
     *
     * @param dto The registration DTO containing user information.
     * @return The created user.
     */
    User saveUser(RegistrationDTO dto);

    /**
     * Updates user based on registration DTO.
     *
     * @param user The user to be updated.
     * @param dto  The registration DTO containing updated information.
     * @return The updated user.
     */
    User updateUser(User user, RegistrationDTO dto);

    /**
     * Deletes the given user.
     *
     * @param user the user to be deleted
     */
    void delete(User user);

}
