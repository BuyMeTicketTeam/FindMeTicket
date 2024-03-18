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
import java.time.temporal.ChronoUnit;
import java.util.*;

@Entity
@Table(name = "ticket")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
@Setter
@Getter
@ToString
@SuperBuilder
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
    private String arrivalTime;

    @Column(name = "arrival_date")
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
        LocalDate date = LocalDate.parse(arrivalDate.replaceAll(",.*", "") + "." + Year.now().getValue(), DateTimeFormatter.ofPattern("d.MM.yyyy"));

        return LocalDateTime.of(date, time);
    }

    public BigDecimal getPrice() {
        throw new UnsupportedOperationException("Method must be only called from subclass");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return carrierEquals(ticket) && departureTimeEquals(ticket) && arrivalTimeEquals(ticket) && Objects.equals(arrivalDate, ticket.arrivalDate);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    private boolean arrivalTimeEquals(Ticket that) {
        if (this.arrivalTime == null || that.arrivalTime == null) return false;

        LocalTime thisArrivalTime = LocalTime.parse(this.arrivalTime);
        LocalTime thatArrivalTime = LocalTime.parse(that.arrivalTime);

        long minutesDifference = Math.abs(ChronoUnit.MINUTES.between(thisArrivalTime, thatArrivalTime));
        return minutesDifference < 5;
    }

    private boolean departureTimeEquals(Ticket that) {
        if (this.departureTime == null || that.departureTime == null) return false;

        LocalTime thisDeparture = LocalTime.parse(this.departureTime);
        LocalTime thatDeparture = LocalTime.parse(that.departureTime);

        long minutesDifference = Math.abs(ChronoUnit.MINUTES.between(thisDeparture, thatDeparture));
        return minutesDifference < 5;
    }

    private boolean carrierEquals(Ticket that) {
        if (this.carrier == null || that.carrier == null) return false;
        String carrierToCompare = that.carrier;

        List<String> result1 = new ArrayList<>();
        List<String> result2 = new ArrayList<>();

        Arrays.stream(this.carrier.split(" ")).toList().forEach(e -> {
            if (e.length() >= 4) result1.add(e);
        });
        Arrays.stream(carrierToCompare.split(" ")).toList().forEach(e -> {
            if (e.length() >= 4) result2.add(e);
        });
        for (String word1 : result1) {
            for (String word2 : result2) {
                if (word2.startsWith(word1) || word1.startsWith(word2)) {
                    return true;
                }
            }
        }
        return false;
    }

}
