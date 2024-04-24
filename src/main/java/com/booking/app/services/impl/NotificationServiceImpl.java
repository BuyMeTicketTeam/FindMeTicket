package com.booking.app.services.impl;

import com.booking.app.entity.User;
import com.booking.app.repositories.UserRepository;
import com.booking.app.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final UserRepository userRepository;

    @Override
    public void disable(User user) {
        user.setNotification(false);
        userRepository.save(user);
    }

    @Override
    public void enable(User user) {
        user.setNotification(true);
        userRepository.save(user);
    }

}
