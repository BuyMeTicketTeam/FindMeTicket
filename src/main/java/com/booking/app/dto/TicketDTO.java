package com.booking.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketDTO {
   private UUID id;
   private String placeFrom;
   private String placeAt;
   private String departureCity;
   private String arrivalCity;
   private String departureTime;
   private String departureDate;
   private String arrivalTime;
   private String arrivalDate;
   private String travelTime;
   private String type;
   private BigDecimal price;
}
