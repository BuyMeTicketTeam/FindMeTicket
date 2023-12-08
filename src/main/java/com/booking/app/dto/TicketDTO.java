package com.booking.app.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketDTO {
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
    //String url;
}
