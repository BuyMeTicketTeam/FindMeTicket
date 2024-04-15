package com.booking.app.services;

import com.booking.app.entity.User;

public interface NotificationService {

    void disable(User user);

    void enable(User user);
}
