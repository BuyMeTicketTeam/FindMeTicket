package com.booking.app.services.impl;

import com.booking.app.entity.UserCredentials;
import com.booking.app.repositories.UserCredentialsRepository;
import com.booking.app.services.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

/**
 * Service class for handling confirmation tokens and user verification.
 */
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final UserCredentialsRepository userCredentialsRepository;

    /**
     * Verifies a given token for a specific user.
     *
     * @param email      The email associated with the user.
     * @param givenToken The token to be verified.
     * @return boolean Returns true if the token is valid and not expired, false otherwise.
     */
    @Override
    public boolean verifyToken(String email, String givenToken) {
        Optional<UserCredentials> userByEmail = userCredentialsRepository.findByEmail(email);
        LocalDateTime now = LocalDateTime.now();
        Date dateExpiryTime = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());

        if (userByEmail.isPresent()) {
            String token = userByEmail.get().getUser().getConfirmToken().getToken();
            if (dateExpiryTime.before(userByEmail.get().getUser().getConfirmToken().getExpiryTime())) {
                if (token.equals(givenToken)) {
                    return true;
                }
            }
        }

        return false;
    }

}
