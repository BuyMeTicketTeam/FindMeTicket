package com.booking.app.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class BusInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(columnDefinition = "varchar(1000)")
    private String link;

    private String sourceWebsite;

    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "bus_ticket_Id")
    private BusTicket busTicket;
}
