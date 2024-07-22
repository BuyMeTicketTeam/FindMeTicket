package com.booking.app.entities.ticket;

import com.google.common.collect.Sets;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name ="route")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "departure_city")
    private String departureCity;

    @Column(name = "arrival_city")
    private String arrivalCity;

    @Column(name = "departure_date")
    private LocalDate departureDate;

    @Column(name = "adding_time")
    private Instant addingTime;

    @OneToMany(mappedBy = "route" ,fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @Builder.Default
    private Set<Ticket> tickets = Sets.newConcurrentHashSet();

}
