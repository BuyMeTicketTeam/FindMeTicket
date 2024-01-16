package com.booking.app.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "ticket")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Ticket {

    @Id
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
    private String travelTime;

    @Column(name = "price")
    private String price;

    @Column(name = "url")
    private String url;

    @ManyToOne
    @JoinColumn(name = "route_id")
    private Route route;

    @OneToMany(mappedBy = "ticket", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<TicketUrls> urls;

}
