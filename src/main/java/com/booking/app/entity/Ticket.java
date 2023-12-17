package com.booking.app.entity;

import jakarta.persistence.*;
import lombok.*;
import org.joda.time.DateTime;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
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

    @Column(name = "departure_city")
    private String departureCity;

    @Column(name = "arrival_city")
    private String arrivalCity;

    @Column(name = "departure_time")
    private String departureTime;

    @Column(name = "departure_date")
    private String departureDate;

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

    @Column(name = "adding_time")
    private LocalDateTime addingTime;
}
