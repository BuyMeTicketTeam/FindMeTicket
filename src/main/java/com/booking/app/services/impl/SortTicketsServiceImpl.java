package com.booking.app.services.impl;

import com.booking.app.dto.RequestSortedTicketsDTO;
import com.booking.app.dto.TicketDto;
import com.booking.app.entity.BusTicket;
import com.booking.app.entity.Ticket;
import com.booking.app.entity.TrainTicket;
import com.booking.app.mapper.BusMapper;
import com.booking.app.mapper.TrainMapper;
import com.booking.app.repositories.RouteRepository;
import com.booking.app.services.SortTicketsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SortTicketsServiceImpl implements SortTicketsService {

    private final RouteRepository routeRepository;

    private final BusMapper busMapper;

    private final TrainMapper trainMapper;

    public List<TicketDto> getSortedTickets(RequestSortedTicketsDTO dto, String language) {
        List<Ticket> tickets = new ArrayList<>(routeRepository.findByDepartureCityAndArrivalCityAndDepartureDate(dto.getDepartureCity(), dto.getArrivalCity(), dto.getDepartureDate()).getTickets().stream().toList());

        List<TicketDto> result = new LinkedList<>();


        Comparator<Ticket> comparator = switch (dto.getSortingBy()) {
            case "Price" -> Comparator.comparing(Ticket::getPrice);
            case "DepartureTime" -> Comparator.comparing(Ticket::getDepartureTime);
            case "ArrivalTime" -> Comparator.comparing(Ticket::formatArrivalDateTime);
            case "TravelTime" -> Comparator.comparing(Ticket::getTravelTime);
            default -> throw new UnsupportedOperationException();
        };

        if(dto.isAscending()) {
            tickets.sort(comparator);
        }else tickets.sort(comparator.reversed());


        for (Ticket ticket : tickets) {
            switch (ticket) {
                case BusTicket t -> {
                    if (dto.getBus())
                        result.add(busMapper.ticketToTicketDto(t, language));
                }
                case TrainTicket t -> {
                    if (dto.getTrain())
                        result.add(trainMapper.toTrainTicketDto(t, language));
                }
                default ->
                        throw new UnsupportedOperationException("Unsupported ticket type: " + ticket.getClass().getSimpleName());
            }

        }

//        Comparator<TicketDto> comparator = switch (dto.getSortingBy()) {
//            case "Price" -> Comparator.comparing(TicketDto::getPrice);
//            case "DepartureTime" -> Comparator.comparing(TicketDto::getDepartureTime);
//            case "ArrivalTime" -> Comparator.comparing(TicketDto::formatArrivalDateTime);
//            case "TravelTime" -> Comparator.comparing(TicketDto::getTravelTime);
//            default -> throw new UnsupportedOperationException();
//        };
//
//
//        result.sort(dto.isAscending()? comparator:comparator.reversed());

        return result;
    }

}
