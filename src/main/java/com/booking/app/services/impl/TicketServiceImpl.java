package com.booking.app.services.impl;

import com.booking.app.dto.RequestSortedTicketsDTO;
import com.booking.app.dto.RequestTicketsDTO;
import com.booking.app.dto.TicketDto;
import com.booking.app.entity.ticket.Route;
import com.booking.app.entity.ticket.Ticket;
import com.booking.app.mapper.BusMapper;
import com.booking.app.mapper.TicketMapper;
import com.booking.app.mapper.TrainMapper;
import com.booking.app.repositories.BusTicketRepository;
import com.booking.app.repositories.RouteRepository;
import com.booking.app.repositories.TrainTicketRepository;
import com.booking.app.services.LanguageDetectorService;
import com.booking.app.services.TicketService;
import com.booking.app.util.TicketComparator;
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

    /**
     * Retrieves bus OR/AND train tickets based on the provided request.
     *
     * @param dto the request containing filtering criteria
     * @param <T> the type of ticket DTO
     * @return a list of tickets or an empty optional if no tickets are found
     * @throws IOException if an I/O error occurs while determining the language
     */
    @Override
    public <T extends TicketDto> Optional<List<T>> getBusTickets(RequestTicketsDTO dto) throws IOException {
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

    public List<TicketDto> getSortedTickets(RequestSortedTicketsDTO dto, String language) {
        List<Ticket> tickets = new ArrayList<>(routeRepository.findByDepartureCityAndArrivalCityAndDepartureDate(dto.getDepartureCity(), dto.getArrivalCity(), dto.getDepartureDate()).getTickets().stream().toList());

        return ticketMapper.toTicketDtoList(TicketComparator.getSortedTickets(tickets, dto), language);

    }

    private String determineLanguage(String word) throws IOException {
        return languageDetectorService.detectLanguage(word).orElse("eng");
    }

}
