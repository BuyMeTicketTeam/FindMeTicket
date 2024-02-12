package com.booking.app.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "ticket_url")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class BusTicket extends Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(columnDefinition = "varchar(1000)")
    private String busforLink;

    private BigDecimal busforPrice;

    @Column(columnDefinition = "varchar(1000)")
    private String infobusLink;

    private BigDecimal infobusPrice;

    @Column(columnDefinition = "varchar(1000)")
    private String proizdLink;

    private BigDecimal proizdPrice;

//    @OneToOne
//    @JoinColumn(name = "ticket_id")
//    private Ticket ticket;
}
