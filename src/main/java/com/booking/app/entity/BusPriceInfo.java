package com.booking.app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Embeddable
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BusPriceInfo {

    @Column(columnDefinition = "varchar(1000)")
    private String link;

    private String sourceWebsite;

    private BigDecimal price;
}
