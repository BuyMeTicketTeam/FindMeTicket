package com.booking.app.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.joda.time.DateTime;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
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
   private String price;
   private String url;
}
