package com.booking.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CitiesDTO {
    private String country;
    private String cityUa;
    private String cityEng;
    private String siteLanguage;
}
