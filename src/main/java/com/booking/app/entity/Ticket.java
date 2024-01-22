package com.booking.app.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "ticket")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "place_from")
    private String placeFrom;

    @Column(name = "place_at")
    private String placeAt;

    @Column(name = "departure_time")
    private String departureTime;

    @Column(name = "arrival_time")
    private String arrivalTime;

    @Column(name = "arrival_date")
    private String arrivalDate;

    @Column(name = "travel_time")
    private BigDecimal travelTime;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "url")
    private String url;

    @ManyToOne
    @JoinColumn(name = "route_id")
    private Route route;

    @OneToOne(mappedBy = "ticket", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private TicketUrls urls;

}
