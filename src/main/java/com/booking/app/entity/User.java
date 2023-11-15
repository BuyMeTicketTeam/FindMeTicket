package com.booking.app.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import java.time.LocalDate;
import java.util.UUID;


@Entity
@Table(name = "users")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;


    @Column(name = "registration_date")
    @CreatedDate
    private LocalDate registrationDate;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id",name = "role_id")
    private Role role;

    @JoinColumn(referencedColumnName = "id", name = "confirm_id")
    @OneToOne
    private ConfirmToken confirmToken;

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    private UserSecurity security;


}
