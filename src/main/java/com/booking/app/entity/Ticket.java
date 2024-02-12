package com.booking.app.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "ticket")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
@Setter
@Getter
@ToString
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Ticket {

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

//    @Column(name = "price")
//    private BigDecimal price;

    @Column(name = "carrier")
    @EqualsAndHashCode.Include
    private String carrier;

//    @Enumerated(EnumType.STRING)
//    private TypeTransportEnum type;

    @ManyToOne
    @JoinColumn(name = "route_id")
    private Route route;

//    @OneToOne(mappedBy = "ticket", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    private TicketUrl urls;

    protected void sad(){

    }

}
