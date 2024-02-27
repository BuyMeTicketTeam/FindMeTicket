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

    public void updateProizdPrice(BigDecimal newProizdPrice) {
        if (newProizdPrice != null) {
            this.proizdPrice = newProizdPrice;
        }
    }

    public void updateInfobusPrice(BigDecimal newInfobusPrice) {
        if (newInfobusPrice != null) {
            this.infobusPrice = newInfobusPrice;
        }
    }

    public void updateBusforPrice(BigDecimal newBusforPrice) {
        if (newBusforPrice != null) {
            this.busforPrice = newBusforPrice;
        }
    }

    @Override
    public BigDecimal getPrice() {
        return Stream.of(proizdPrice, infobusPrice, busforPrice).min(Comparator.nullsLast(BigDecimal::compareTo)).get();
    }

}
