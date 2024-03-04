package com.booking.app.services.impl;


import com.booking.app.constant.PopularRoutesConstants;
import com.booking.app.dto.RequestTicketsDTO;
import com.booking.app.entity.UkrainianPlaces;
import com.booking.app.repositories.UkrPlacesRepository;
import com.booking.app.services.PopularRoutesService;
import com.booking.app.util.City;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PopularRoutesServiceImpl implements PopularRoutesService {

    private final ScraperManager manager;

    private final UkrPlacesRepository placesRepository;

    @Override
    @Async
    public void findRoutes() throws IOException, ParseException {
        for (City popularRoute : PopularRoutesConstants.getPopularRoutes()) {
            Optional<UkrainianPlaces> departureCity
                    = placesRepository.findById(popularRoute.departureId());
            Optional<UkrainianPlaces> arrivalCity
                    = placesRepository.findById(popularRoute.arrivalId());

            if (departureCity.isPresent() && arrivalCity.isPresent()) {
                manager.scrapeTickets(RequestTicketsDTO.builder()
                        .departureCity(departureCity.get().getNameUa())
                        .arrivalCity(arrivalCity.get().getNameUa())
                        .departureDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-M-d")))
                        .build(), null, "ua");
                manager.scrapeTickets(RequestTicketsDTO.builder()
                        .departureCity(departureCity.get().getNameEng())
                        .arrivalCity(arrivalCity.get().getNameEng())
                        .departureDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-M-d")))
                        .build(), null, "eng");
            }
        }
    }

}
