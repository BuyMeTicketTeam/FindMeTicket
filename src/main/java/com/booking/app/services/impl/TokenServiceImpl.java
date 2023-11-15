package com.booking.app.services.impl;

import com.booking.app.entity.ConfirmToken;
import com.booking.app.entity.User;
import com.booking.app.entity.UserSecurity;
import com.booking.app.repositories.UserSecurityRepository;
import com.booking.app.services.MailSenderService;
import com.booking.app.services.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    @Value("${TOKEN_SYMBOLS}")
    private String TOKEN_SYMBOLS;
    private final UserSecurityRepository userSecurityRepository;

    @Override
    public ConfirmToken createConfirmToken(User user) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tenMinutes = now.plusMinutes(10);
        Date dateExpiryTime = Date.from(tenMinutes.atZone(ZoneId.systemDefault()).toInstant());
        return ConfirmToken.builder()
                .user(user)
                .expiryTime(dateExpiryTime)
                .token(generateRandomToken())
                .user(user)
                .build();
    }

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
}
