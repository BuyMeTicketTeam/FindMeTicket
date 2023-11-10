package com.booking.app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "verify_email")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerifyEmail {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(mappedBy = "verifyEmail",cascade = CascadeType.ALL)
    private User user;

    private String token;

    private LocalDateTime expiryDate;
}

