package com.booking.app.entity;

import jakarta.persistence.*;
import lombok.Builder;
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


    @OneToMany(mappedBy = "busTicket" ,fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @Builder.Default
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

    public BusTicket addPrice(BusPriceInfo busPriceInfo) {
        busPriceInfo.setBusTicket(this);
        infoList.add(busPriceInfo);
        return this;
    }
}
