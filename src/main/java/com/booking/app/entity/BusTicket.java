package com.booking.app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;

import java.math.BigDecimal;

@Entity
@DiscriminatorValue("BUS")
@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
@DynamicUpdate
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

}
