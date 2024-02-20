package com.booking.app.services.impl;

import com.booking.app.dto.BusTicketDTO;
import com.booking.app.dto.RequestSortedTicketsDTO;
import com.booking.app.entity.BusTicket;
import com.booking.app.entity.Route;
import com.booking.app.mapper.BusMapper;
import com.booking.app.repositories.BusTicketRepository;
import com.booking.app.repositories.RouteRepository;
import com.booking.app.services.TicketService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    RouteRepository routeRepository;

    BusTicketRepository busTicketRepository;

    BusMapper busMapper;

    public List<BusTicketDTO> getBusTickets(RequestSortedTicketsDTO dto) {
        Route route = routeRepository.findByDepartureCityAndArrivalCityAndDepartureDate(dto.getDepartureCity(), dto.getArrivalCity(), dto.getDepartureDate());
        List<BusTicketDTO> busTicketDTO = new ArrayList<>();
        if (Objects.nonNull(route)) {
            List<BusTicket> busTickets = busTicketRepository.findByRoute(route);

            busTickets.forEach(busTicket -> busTicketDTO.add(busMapper.ticketToTicketDto(busTicket, "ua")));
        }
        return busTicketDTO;
    }

}
