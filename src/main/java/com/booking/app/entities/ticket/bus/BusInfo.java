package com.booking.app.entities.ticket.bus;

import com.booking.app.constants.Website;
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

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "website", insertable = false, updatable = false)
    private Website website;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "bus_ticket_Id")
    private BusTicket busTicket;
}
