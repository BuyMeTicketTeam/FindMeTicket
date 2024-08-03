package com.booking.app.entities.ticket.bus;

import com.booking.app.entities.ticket.Ticket;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@DiscriminatorValue("BUS")
@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
//@EqualsAndHashCode(callSuper = true) --> doesn't work
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

    public BusTicket addPrice(BusInfo busInfo) {
        busInfo.setBusTicket(this);
        infoList.add(busInfo);
        return this;
    }
}
