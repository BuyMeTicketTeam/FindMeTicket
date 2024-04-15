package com.booking.app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "token")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ConfirmToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(mappedBy = "confirmToken")
    private User user;

    @NotNull
    private String token;

    @NotNull
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date expiryTime;

    public static ConfirmToken createConfirmToken() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tenMinutes = now.plusMinutes(10);
        Date dateExpiryTime = Date.from(tenMinutes.atZone(ZoneId.systemDefault()).toInstant());
        return ConfirmToken.builder()
                .expiryTime(dateExpiryTime)
                .token(generateRandomToken())
                .build();
    }

    private static String generateRandomToken() {
        String tokenSymbols = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        final SecureRandom random = new SecureRandom();
        StringBuilder stringBuilder = new StringBuilder(5);
        for (int i = 0; i < 5; i++) {
            int randomIndex = random.nextInt(tokenSymbols.length());
            char randomChar = tokenSymbols.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }
        return stringBuilder.toString();
    }

}

