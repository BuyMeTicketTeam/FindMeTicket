package com.booking.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Builder
public class UrlAndPriceDTO {
    private BigDecimal price;
    private String url;
}
