package com.booking.app.services.impl;

import com.booking.app.dto.tickets.RequestSortedTicketsDto;
import com.booking.app.dto.tickets.RequestTicketsDto;
import com.booking.app.dto.tickets.ResponseTicketDto;
import com.booking.app.entities.ticket.Route;
import com.booking.app.entities.ticket.Ticket;
import com.booking.app.mappers.BusMapper;
import com.booking.app.mappers.TicketMapper;
import com.booking.app.mappers.TrainMapper;
import com.booking.app.repositories.BusTicketRepository;
import com.booking.app.repositories.RouteRepository;
import com.booking.app.repositories.TrainTicketRepository;
import com.booking.app.services.LanguageDetectorService;
import com.booking.app.services.TicketService;
import com.booking.app.utils.tickets.TicketComparator;
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

    TicketMapper ticketMapper;

    @Override
    public <T extends ResponseTicketDto> Optional<List<T>> getTickets(RequestTicketsDto dto) throws IOException {
        Route route = findRoute(dto);

        if (Objects.nonNull(route)) {
            List<T> ticketDto = new ArrayList<>();
            String language = determineLanguage(route.getDepartureCity());
            addTicketsToDto(dto, route, ticketDto, language);
            return Optional.of(ticketDto);
        }

        return Optional.empty();
    }

    @Override
    public List<ResponseTicketDto> getSortedTickets(RequestSortedTicketsDto dto, String language) {
        List<Ticket> tickets = new ArrayList<>(routeRepository.findByDepartureCityAndArrivalCityAndDepartureDate(dto.getDepartureCity(), dto.getArrivalCity(), dto.getDepartureDate()).getTickets().stream().toList());
        if (Objects.nonNull(dto.getSortingBy())) {
            tickets = TicketComparator.sort(tickets, dto);
        }
        return ticketMapper.toTicketDtoList(tickets, language);
    }

    /**
     * Adds tickets to the DTO list based on the request criteria.
     *
     * @param dto       the request containing filtering criteria
     * @param route     the route for which tickets are to be fetched
     * @param ticketDto the list to which the ticket DTOs are added
     * @param language  the language for the ticket details
     * @param <T>       the type of ticket DTO
     */
    private <T extends ResponseTicketDto> void addTicketsToDto(RequestTicketsDto dto, Route route, List<T> ticketDto, String language) {
        if (Boolean.TRUE.equals(dto.getBus())) {
            busTicketRepository.findByRoute(route)
                    .ifPresent(busTickets -> busTickets.forEach(bus -> ticketDto.add((T) busMapper.ticketToTicketDto(bus, language))));
        }

        if (Boolean.TRUE.equals(dto.getTrain())) {
            trainTicketRepository.findByRoute(route)
                    .ifPresent(trainTickets -> trainTickets.forEach(train -> ticketDto.add((T) trainMapper.toTrainTicketDto(train, language))));
        }
    }


    /**
     * Finds the route based on the request criteria.
     *
     * @param dto the request containing filtering criteria
     * @return the route that matches the criteria
     */
    private Route findRoute(RequestTicketsDto dto) {
        return routeRepository.findByDepartureCityAndArrivalCityAndDepartureDate(
                dto.getDepartureCity(),
                dto.getArrivalCity(),
                dto.getDepartureDate()
        );
    }

    /**
     * Determines the language of the provided word.
     *
     * @param word the word for which the language is to be detected
     * @return the detected language or "eng" if detection fails
     * @throws IOException if an I/O error occurs while determining the language
     */
    private String determineLanguage(String word) throws IOException {
        return languageDetectorService.detectLanguage(word).orElse("eng");
    }

}
