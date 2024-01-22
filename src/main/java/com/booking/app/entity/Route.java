package com.booking.app.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name ="route")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString(exclude = "ticketList")
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

    @OneToMany(mappedBy = "route" ,fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Ticket> ticketList;

}
