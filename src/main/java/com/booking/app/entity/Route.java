package com.booking.app.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name ="route")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Route {

    @Id
    private UUID id;

    @Column(name = "departure_city")
    private String departureCity;

    @Column(name = "arrival_city")
    private String arrivalCity;

    @Column(name = "departure_date")
    private String departureDate;

    @Column(name = "adding_time")
    private LocalDateTime addingTime;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "route", cascade = CascadeType.ALL)
    private List<Ticket> ticketList;
}
