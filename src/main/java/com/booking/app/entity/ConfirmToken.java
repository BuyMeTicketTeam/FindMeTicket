package com.booking.app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "confirm_token")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
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

