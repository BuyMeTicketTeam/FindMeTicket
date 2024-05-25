package com.booking.app.services;

import java.util.UUID;

public interface UserService {

    /**
     * Updates the notification setting for a user.
     *
     * @param uuid         the ID of the user
     * @param notification the new notification setting
     */
    void updateNotification(UUID uuid, boolean notification);

}
