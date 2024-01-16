package com.booking.app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "token")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ConfirmToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

//    @OneToOne(mappedBy = "confirmToken", cascade = CascadeType.ALL)
    @OneToOne(mappedBy = "confirmToken")
    private User user;

    @NotNull
    private String token;

    @NotNull
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date expiryTime;

}

