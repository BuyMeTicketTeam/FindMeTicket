package com.booking.app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "train_info", joinColumns = @JoinColumn(name = "train_ticket_id"))
    @Column(name = "train_info")
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
        infoList.addAll(trainTicket.getInfoList());
        return this;
    }
}
