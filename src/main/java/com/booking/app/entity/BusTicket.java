package com.booking.app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.stream.Stream;

@Entity
@DiscriminatorValue("BUS")
@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
//@EqualsAndHashCode(callSuper = true) --> doesn't work
public class BusTicket extends Ticket {

    @Column(columnDefinition = "varchar(1000)")
    private String busforLink;

    private BigDecimal busforPrice;

    @Column(columnDefinition = "varchar(1000)")
    private String infobusLink;

    private BigDecimal infobusPrice;

    @Column(columnDefinition = "varchar(1000)")
    private String proizdLink;

    private BigDecimal proizdPrice;

    public boolean linksAreScraped() {
        return busforLink != null || proizdLink != null || infobusLink != null;
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
        return Stream.of(proizdPrice, infobusPrice, busforPrice).min(Comparator.nullsLast(BigDecimal::compareTo)).get();
    }

    public BusTicket addPrices(BusTicket busTicket) {
        if (busforPrice == null) busforPrice = busTicket.getBusforPrice();
        if (proizdPrice == null) proizdPrice = busTicket.getProizdPrice();
        if (infobusPrice == null) infobusPrice = busTicket.getInfobusPrice();
        return this;
    }
}
