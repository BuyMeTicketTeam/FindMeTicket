package com.booking.app.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "ticket_urls")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class TicketUrls {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    private String busfor;

    private String infobus;

    private String proizd;

    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;
}
