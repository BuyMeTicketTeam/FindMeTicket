package com.booking.app.entity;

import io.micrometer.core.annotation.Timed;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.sql.Time;
import java.util.Date;
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

    @OneToOne(mappedBy = "verifyEmail", cascade = CascadeType.ALL)
    private User user;

    @NotNull
    private String token;

    @NotNull
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date expiryTime;



}

