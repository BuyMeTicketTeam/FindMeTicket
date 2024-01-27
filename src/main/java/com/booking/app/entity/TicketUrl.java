package com.booking.app.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "ticket_url")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class TicketUrl {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(columnDefinition = "varchar(1000)")
    private String busfor;

    @Column(columnDefinition = "varchar(1000)")
    private String infobus;

    @Column(columnDefinition = "varchar(1000)")
    private String proizd;

    @OneToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;
}
