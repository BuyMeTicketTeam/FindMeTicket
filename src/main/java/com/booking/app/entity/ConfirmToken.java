package com.booking.app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "confirm_token")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfirmToken {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(mappedBy = "confirmToken", cascade = CascadeType.ALL)
    private User user;

    @NotNull
    private String token;

    @NotNull
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date expiryTime;



}

