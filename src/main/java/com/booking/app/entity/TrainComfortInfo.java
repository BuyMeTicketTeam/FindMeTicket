package com.booking.app.entity;

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
public class TrainComfortInfo {

        private String link;

        private String comfort;

        // might be removed later
        private String placesLeft;

        private BigDecimal price;

}
