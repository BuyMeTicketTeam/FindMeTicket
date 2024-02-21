package com.booking.app.services.impl;

import com.booking.app.dto.RequestSortedTicketsDTO;
import com.booking.app.dto.TicketDto;
import com.booking.app.entity.Route;
import com.booking.app.mapper.BusMapper;
import com.booking.app.mapper.TrainMapper;
import com.booking.app.repositories.BusTicketRepository;
import com.booking.app.repositories.RouteRepository;
import com.booking.app.repositories.TrainTicketRepository;
import com.booking.app.services.LanguageDetectorService;
import com.booking.app.services.TicketService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    RouteRepository routeRepository;

    BusTicketRepository busTicketRepository;

    TrainTicketRepository trainTicketRepository;

    LanguageDetectorService languageDetectorService;

    BusMapper busMapper;

    TrainMapper trainMapper;

    @Override
    public <T extends TicketDto> Optional<List<T>> getBusTickets(RequestSortedTicketsDTO dto) throws IOException {
        Route route = routeRepository.findByDepartureCityAndArrivalCityAndDepartureDate(dto.getDepartureCity(), dto.getArrivalCity(), dto.getDepartureDate());

        if (Objects.nonNull(route)) {
            List<T> ticketDto = new ArrayList<>();
            String language = determineLanguage(route.getDepartureCity());

            if (Boolean.TRUE.equals(dto.getBus())) {
                busTicketRepository.findByRoute(route)
                        .ifPresent(busTickets -> busTickets.forEach(bus -> ticketDto.add((T) busMapper.ticketToTicketDto(bus, language))));
            }

            if (Boolean.TRUE.equals(dto.getTrain())) {
                trainTicketRepository.findByRoute(route)
                        .ifPresent(trainTickets -> trainTickets.forEach(train -> ticketDto.add((T) trainMapper.toTrainTicketDto(train, language))));
            }

            return Optional.of(ticketDto);
        }

        return Optional.empty();
    }

    private String determineLanguage(String word) throws IOException {
        return languageDetectorService.detectLanguage(word).orElse("eng");
    }

}
