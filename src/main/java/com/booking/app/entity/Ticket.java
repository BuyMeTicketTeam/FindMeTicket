package com.booking.app.entity;

import com.booking.app.enums.TypeTransportEnum;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "ticket")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Ticket {

    @Id
    private UUID id;

    @Column(name = "place_from")
    private String placeFrom;

    @Column(name = "place_at")
    private String placeAt;

    @Column(name = "departure_time")
    @EqualsAndHashCode.Include
    private String departureTime;

    @Column(name = "arrival_time")
    @EqualsAndHashCode.Include
    private String arrivalTime;

    @Column(name = "arrival_date")
    @EqualsAndHashCode.Include
    private String arrivalDate;

    @Column(name = "travel_time")
    private BigDecimal travelTime;

    @Column(name = "price")
    @EqualsAndHashCode.Include
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private TypeTransportEnum type;

    @ManyToOne
    @JoinColumn(name = "route_id")
    private Route route;

    @OneToOne(mappedBy = "ticket", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private TicketUrls urls;

}
