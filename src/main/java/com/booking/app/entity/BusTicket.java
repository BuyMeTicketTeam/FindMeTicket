package com.booking.app.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Entity
@DiscriminatorValue("BUS")
@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
//@EqualsAndHashCode(callSuper = true) --> doesn't work
public class BusTicket extends Ticket {

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "bus_info", joinColumns = @JoinColumn(name = "bus_ticket_id"))
    @Column(name = "bus_info")
    List<BusPriceInfo> infoList = new ArrayList<>();

    public boolean linksAreScraped() {
        return infoList.stream().anyMatch(t->t.getLink()!=null);
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
        return infoList.stream().map(BusPriceInfo::getPrice).min(Comparator.nullsLast(BigDecimal::compareTo)).get();
    }

    public BusTicket addPrices(BusTicket busPriceInfo) {
        infoList.addAll(busPriceInfo.getInfoList());
        return this;
    }
}
