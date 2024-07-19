package com.booking.app.entities.ticket;

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
import java.util.Objects;
import java.util.UUID;

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
        String thisCarrier = this.carrier;
        String thatCarrier = that.carrier;

        if (thisCarrier == null || thatCarrier == null) return false;

        // Context specific methods checks (not recommendable in prod)
        if (specificCarrierLanguage(thisCarrier, thatCarrier)) return true;
        if (specificCarrierLanguages(thisCarrier, thatCarrier)) return true;
        if (!specificCarrierEquality(thisCarrier, thatCarrier)) return false;

        char[] charArray = thatCarrier.toCharArray();
        for (int i = 0; i < charArray.length - 4; i += 4) {
            String quadruple = String.valueOf(charArray[i]) + charArray[i + 1] + charArray[i + 2] + charArray[i + 3];
            if (thisCarrier.contains(quadruple)) return true;
        }
        return false;
    }

    // Context specific method, not useful in production!
    private boolean specificCarrierEquality(String firstCarrier, String secondCarrier) {
        if (!firstCarrier.equals(secondCarrier)) {
            if (firstCarrier.equals("ПАВЛЮК В.І.") && secondCarrier.equals("ПАВЛЮК М.І.")) return false;
            return !secondCarrier.equals("ПАВЛЮК В.І.") || !firstCarrier.equals("ПАВЛЮК М.І.");

        }
        return true;
    }

    // Context specific method, not useful in production!
    private boolean specificCarrierLanguage(String firstCarrier, String secondCarrier) {
        if (firstCarrier.equals("ПАВЛЮКС - ТРАНС") && secondCarrier.equals("PAVLUKS - TRANS")) return true;
        return secondCarrier.equals("ПАВЛЮКС - ТРАНС") && firstCarrier.equals("PAVLUKS - TRANS");
    }

    // Context specific method, not useful in production!
    private boolean specificCarrierLanguages(String firstCarrier, String secondCarrier) {
        if (firstCarrier.equals("ДЕНИСІВКА") && secondCarrier.equals("DENYSIVKA")) return true;
        return secondCarrier.equals("ДЕНИСІВКА") && firstCarrier.equals("DENYSIVKA");
    }

}
