package com.booking.app.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
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

    public LocalDateTime formatArrivalDateTime() {
        LocalTime time = LocalTime.parse(arrivalTime, DateTimeFormatter.ofPattern("HH:mm"));
        LocalDate date = LocalDate.parse(arrivalDate.replaceAll(",.*", "")+"."+ Year.now().getValue(), DateTimeFormatter.ofPattern("d.MM.yyyy"));

        return LocalDateTime.of(date, time);
    }

    public BigDecimal getPrice() {
        return BigDecimal.valueOf(0);
    }
}
