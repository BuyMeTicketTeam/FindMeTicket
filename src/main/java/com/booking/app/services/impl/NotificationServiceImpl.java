package com.booking.app.services.impl;

import com.booking.app.repositories.UserCredentialsRepository;
import com.booking.app.services.NotificationService;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final UserCredentialsRepository userCredentialsRepository;

    @Override
    public void disable(Cookie id) {
        try {
            UUID userId = UUID.fromString(id.getValue());
            userCredentialsRepository.findById(userId).ifPresent(u -> {
                u.getUser().setNotification(false);
                userCredentialsRepository.save(u);
            });
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("No user by %s id", id), e);
        }
    }

    @Override
    public void enable(Cookie id) {
        try {
            UUID userId = UUID.fromString(id.getValue());
            userCredentialsRepository.findById(userId).ifPresent(u -> {
                u.getUser().setNotification(true);
                userCredentialsRepository.save(u);
            });
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("No user by %s id", id), e);
        }
    }

}
