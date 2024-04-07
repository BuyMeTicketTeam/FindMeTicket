package com.booking.app.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Entity
@DiscriminatorValue("TRAIN")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@SuperBuilder
@DynamicUpdate
public class TrainTicket extends Ticket {

    @OneToMany(mappedBy = "trainTicket" ,fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @Builder.Default
    List<TrainComfortInfo> infoList = new ArrayList<>();

    @Override
    public BigDecimal getPrice() {
        return infoList.stream().map(t -> t.getPrice()).min(Comparator.nullsLast(BigDecimal::compareTo)).get();
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public TrainTicket addPrices(TrainTicket trainTicket) {
        trainTicket.getInfoList().forEach(t->{
            t.setTrainTicket(this);
            infoList.add(t);
        });
        return this;
    }
}
