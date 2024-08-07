package com.booking.app.entities.ticket;

import com.booking.app.constants.TransportType;
import com.booking.app.entities.converters.DurationConverter;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Entity
@Table(name = "ticket")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
        name = "type",
        discriminatorType = DiscriminatorType.STRING
)
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

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "transport", insertable = false, updatable = false)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private TransportType type;

    @Column(name = "departure_time")
    @EqualsAndHashCode.Include
    private LocalTime departureTime;

    @Column(name = "arrival_date_time")
    private Instant arrivalDateTime;

    @Column(name = "travel_time")
    @Convert(converter = DurationConverter.class)
    private Duration travelTime;

    @Column(name = "carrier")
    @EqualsAndHashCode.Include
    private String carrier;

    @ManyToOne
    @JoinColumn(name = "route_id")
    private Route route;

    public BigDecimal getPrice() {
        throw new UnsupportedOperationException("Method must be only called from subclass");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return carrierEquals(ticket) && arrivalDateTimeEquals(ticket) && departureTimeEquals(ticket);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    /**
     * Compares the arrivalDateTime of this Ticket with that Ticket.
     * The reason for that: on different websites same ticket might have arrival datetime differs within 5 minutes.
     *
     * @param that the other Ticket to compare with
     * @return true if the arrivalDateTime values of both Tickets are within 5 minutes of each other, false otherwise
     */
    private boolean arrivalDateTimeEquals(Ticket that) {
        if (this.arrivalDateTime == null || that.arrivalDateTime == null) {
            return false;
        }
        return Math.abs(ChronoUnit.MINUTES.between(this.arrivalDateTime, that.arrivalDateTime)) < 5;
    }

    /**
     * Compares the departureTime of this Ticket with that Ticket.
     * The reason for that: on different websites same ticket might have departure time differs within 5 minutes.
     *
     * @param that the other Ticket to compare with
     * @return true if the departureTime values of both Tickets are within 5 minutes of each other, false otherwise
     */
    private boolean departureTimeEquals(Ticket that) {
        if (this.departureTime == null || that.departureTime == null) {
            return false;
        }
        return Math.abs(ChronoUnit.MINUTES.between(this.departureTime, that.departureTime)) < 5;
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
