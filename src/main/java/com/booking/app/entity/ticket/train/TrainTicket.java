package com.booking.app.entity.ticket.train;

import com.booking.app.entity.ticket.Ticket;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public TrainTicket addPrices(List<TrainInfo> trainInfos) {
        trainInfos.forEach(t -> {
            t.setTrainTicket(this);
            infoList.add(t);
        });
        return this;
    }

    public TrainTicket addPrice(TrainInfo busInfo) {
        busInfo.setTrainTicket(this);
        infoList.add(busInfo);
        return this;
    }
}
