package com.booking.app.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@DiscriminatorValue("BUS")
@Setter
@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
//@EqualsAndHashCode(callSuper = true) --> not working)))))))
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

//    @OneToOne
//    @JoinColumn(name = "ticket_id")
//    private Ticket ticket;

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

}
