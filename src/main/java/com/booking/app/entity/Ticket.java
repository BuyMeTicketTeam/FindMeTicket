package com.booking.app.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "ticket")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
@Setter
@Getter
@ToString
@SuperBuilder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@DynamicUpdate
@NoArgsConstructor
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

    @Column(name = "carrier")
    @EqualsAndHashCode.Include
    private String carrier;

    @ManyToOne
    @JoinColumn(name = "route_id")
    private Route route;

}
