package com.booking.app.entities.ticket.bus;

import com.booking.app.constants.Website;
import com.booking.app.entities.ticket.Route;
import com.booking.app.entities.ticket.Ticket;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@DiscriminatorValue("BUS")
@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
public class BusTicket extends Ticket {

    @OneToMany(mappedBy = "busTicket", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Builder.Default
    List<BusInfo> infoList = new ArrayList<>();

    public boolean linksPresent() {
        return infoList.stream().anyMatch(t -> t.getLink() != null);
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public BigDecimal getPrice() {
        return infoList.stream()
                .map(BusInfo::getPrice)
                .filter(Objects::nonNull)
                .min(BigDecimal::compareTo)
                .map(price -> price.setScale(2, RoundingMode.UNNECESSARY))
                .orElse(null);
    }

    public static BusTicket createInstance(Route route,
                                           String placeFrom,
                                           String placeAt,
                                           LocalTime departureTime,
                                           Instant arrivalDateTime,
                                           Duration travelTime,
                                           String carrier,
                                           BigDecimal price,
                                           Website website,
                                           String link) {
        BusTicket busTicket = BusTicket.builder()
                .id(UUID.randomUUID())
                .route(route)
                .placeFrom(placeFrom)
                .placeAt(placeAt)
                .departureTime(departureTime)
                .arrivalDateTime(arrivalDateTime)
                .travelTime(travelTime)
                .carrier(carrier).build();
        busTicket.addBusInfo(
                BusInfo.builder().price(price)
                        .website(website)
                        .link(link)
                        .build()
        );
        return busTicket;
    }

    public void addBusInfo(BusInfo busInfo) {
        busInfo.setBusTicket(this);
        infoList.add(busInfo);
    }

}
