package com.booking.app.services.impl;

import com.booking.app.repositories.UserRepository;
import com.booking.app.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Service implementation for managing user operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Override
    public void updateNotification(UUID uuid, boolean notification) {
        repository.updateByNotification(uuid, notification);
    }

}