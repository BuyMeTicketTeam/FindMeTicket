package com.booking.app.entity.ticket;

import com.booking.app.entity.ticket.Ticket;
import com.google.common.collect.Sets;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
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
    private String departureDate;

    @Column(name = "adding_time")
    private LocalDateTime addingTime;

    @OneToMany(mappedBy = "route" ,fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @Builder.Default
    private Set<Ticket> tickets = Sets.newConcurrentHashSet();

}
