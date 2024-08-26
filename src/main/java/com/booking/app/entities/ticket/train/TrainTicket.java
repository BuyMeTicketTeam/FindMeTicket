package com.booking.app.entities.ticket.train;

import com.booking.app.entities.ticket.Route;
import com.booking.app.entities.ticket.Ticket;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;

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
@DiscriminatorValue("TRAIN")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@SuperBuilder
@DynamicUpdate
public class TrainTicket extends Ticket {

    @OneToMany(mappedBy = "trainTicket", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Builder.Default
    List<TrainInfo> infoList = new ArrayList<>();

    @Override
    public BigDecimal getPrice() {
        return infoList.stream()
                .map(t -> t.getPrice())
                .filter(Objects::nonNull)
                .min(BigDecimal::compareTo)
                .map(price -> price.setScale(2, RoundingMode.UNNECESSARY))
                .orElse(null);
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public static TrainTicket createInstance(Route route,
                                             String placeFrom,
                                             String placeAt,
                                             LocalTime departureTime,
                                             Instant arrivalDateTime,
                                             Duration travelTime,
                                             String carrier,
                                             List<TrainInfo> infoList) {
        TrainTicket trainTicket = TrainTicket.builder()
                .id(UUID.randomUUID())
                .route(route)
                .placeFrom(placeFrom)
                .placeAt(placeAt)
                .arrivalDateTime(arrivalDateTime)
                .departureTime(departureTime)
                .travelTime(travelTime)
                .carrier(carrier)
                .build();
        trainTicket.addTrainInfo(infoList);
        return trainTicket;
    }

    public void addTrainInfo(List<TrainInfo> trainInfos) {
        trainInfos.forEach(t -> {
            t.setTrainTicket(this);
            infoList.add(t);
        });
    }

}
