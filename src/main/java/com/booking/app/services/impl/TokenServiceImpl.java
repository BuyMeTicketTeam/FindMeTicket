package com.booking.app.services.impl;

import com.booking.app.entity.User;
import com.booking.app.repositories.UserCredentialsRepository;
import com.booking.app.services.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Service class for handling confirmation tokens and user verification.
 */
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final UserCredentialsRepository userCredentialsRepository;

    @Override
    public boolean verifyToken(String email, String givenToken) {
        LocalDateTime now = LocalDateTime.now();
        Date dateExpiryTime = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
        return userCredentialsRepository.findByEmail(email).map(userCredentials -> {
            User user = userCredentials.getUser();
            String token = user.getConfirmToken().getToken();
            return dateExpiryTime.before(user.getConfirmToken().getExpiryTime()) && token.equals(givenToken);
        }).orElse(false);
    }

}
