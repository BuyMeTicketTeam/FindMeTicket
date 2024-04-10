package com.booking.app.services.impl;


import com.booking.app.constant.PopularRoutesConstants;
import com.booking.app.dto.RequestTicketsDTO;
import com.booking.app.entity.UkrainianPlaces;
import com.booking.app.repositories.UkrPlacesRepository;
import com.booking.app.services.PopularRoutesService;
import com.booking.app.services.impl.scrape.ScraperManager;
import com.booking.app.dto.City;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PopularRoutesServiceImpl implements PopularRoutesService {

    public static final String PATTERN = "yyyy-M-d";
    private final ScraperManager manager;

    private final UkrPlacesRepository placesRepository;

    @Override
    @Async
    public void findRoutes() throws IOException, ParseException {

        List<City> routes = PopularRoutesConstants.getPopularRoutes();
        List<CompletableFuture<Boolean>> runningRoutes = new LinkedList<>();

        for (int i = 0; i < routes.size(); i++) {

            Optional<UkrainianPlaces> departureCity
                    = placesRepository.findById(routes.get(i).departureId());
            Optional<UkrainianPlaces> arrivalCity
                    = placesRepository.findById(routes.get(i).arrivalId());
//
            if (departureCity.isPresent() && arrivalCity.isPresent()) {
                runningRoutes.add(manager.findTickets(RequestTicketsDTO.builder()
                        .departureCity(departureCity.get().getNameUa())
                        .arrivalCity(arrivalCity.get().getNameUa())
                        .departureDate(LocalDate.now().format(DateTimeFormatter.ofPattern(PATTERN)))
                        .build(), null, "ua"));
                runningRoutes.add(manager.findTickets(RequestTicketsDTO.builder()
                        .departureCity(departureCity.get().getNameEng())
                        .arrivalCity(arrivalCity.get().getNameEng())
                        .departureDate(LocalDate.now().format(DateTimeFormatter.ofPattern(PATTERN)))
                        .build(), null, "eng"));

                runningRoutes.add(manager.findTickets(RequestTicketsDTO.builder()
                        .departureCity(arrivalCity.get().getNameUa())
                        .arrivalCity(departureCity.get().getNameUa())
                        .departureDate(LocalDate.now().format(DateTimeFormatter.ofPattern(PATTERN)))
                        .build(), null, "ua"));
                runningRoutes.add(manager.findTickets(RequestTicketsDTO.builder()
                        .departureCity(arrivalCity.get().getNameEng())
                        .arrivalCity(departureCity.get().getNameEng())
                        .departureDate(LocalDate.now().format(DateTimeFormatter.ofPattern(PATTERN)))
                        .build(), null, "eng"));


                CompletableFuture.allOf(runningRoutes.toArray((CompletableFuture[]::new))).join();

                runningRoutes.clear();
            }

        }
    }

}
