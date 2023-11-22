package com.booking.app.services.impl;

import com.booking.app.entity.ConfirmToken;
import com.booking.app.entity.User;
import com.booking.app.entity.UserSecurity;
import com.booking.app.repositories.UserSecurityRepository;
import com.booking.app.services.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;
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

    @Value("${TOKEN_SYMBOLS}")
    private String TOKEN_SYMBOLS;
    private final UserSecurityRepository userSecurityRepository;

    /**
     * Verifies a given token for a specific user.
     *
     * @param email The email associated with the user.
     * @param givenToken The token to be verified.
     * @return boolean Returns true if the token is valid and not expired, false otherwise.
     */
    @Override
    public boolean verifyToken(String email, String givenToken) {
        Optional<UserSecurity> userByEmail = userSecurityRepository.findByEmail(email);
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

    /**
     * Creates a confirmation token for the given user.
     *
     * @param user The user for whom the confirmation token is created.
     * @return ConfirmToken An object containing the user, expiry time, and a randomly generated token.
     */
    @Override
    public ConfirmToken createConfirmToken(User user) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tenMinutes = now.plusMinutes(10);
        Date dateExpiryTime = Date.from(tenMinutes.atZone(ZoneId.systemDefault()).toInstant());
        return ConfirmToken.builder()
                .user(user)
                .expiryTime(dateExpiryTime)
                .token(generateRandomToken())
                .build();
    }

    /**
     * Generates a random token consisting of characters from a predefined set.
     *
     * @return String A randomly generated token.
     */
    @Override
    public String generateRandomToken() {
        final SecureRandom random = new SecureRandom();
        StringBuilder stringBuilder = new StringBuilder(5);
        for (int i = 0; i < 5; i++) {
            int randomIndex = random.nextInt(TOKEN_SYMBOLS.length());
            char randomChar = TOKEN_SYMBOLS.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }
        return stringBuilder.toString();
    }


}
