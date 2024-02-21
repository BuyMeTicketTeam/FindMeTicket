package com.booking.app.services.impl;

import com.booking.app.dto.RequestSortedTicketsDTO;
import com.booking.app.dto.TicketDto;
import com.booking.app.entity.BusTicket;
import com.booking.app.entity.Route;
import com.booking.app.entity.TrainTicket;
import com.booking.app.mapper.BusMapper;
import com.booking.app.mapper.TrainMapper;
import com.booking.app.repositories.BusTicketRepository;
import com.booking.app.repositories.RouteRepository;
import com.booking.app.repositories.TrainTicketRepository;
import com.booking.app.services.TicketService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

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

    BusMapper busMapper;

    TrainMapper trainMapper;

    public <T extends TicketDto> Optional<List<T>> getBusTickets(RequestSortedTicketsDTO dto) {
        Route route = routeRepository.findByDepartureCityAndArrivalCityAndDepartureDate(dto.getDepartureCity(), dto.getArrivalCity(), dto.getDepartureDate());

        if (Objects.nonNull(route)) {
            List<T> ticketDTOs = new ArrayList<>();

            if (dto.getBus()) {
                List<BusTicket> busTickets = busTicketRepository.findByRoute(route);
                busTickets.forEach(bus -> ticketDTOs.add((T) busMapper.ticketToTicketDto(bus, "ua")));
            }

            if (dto.getTrain()) {
                List<TrainTicket> trainTickets = trainTicketRepository.findByRoute(route);
                trainTickets.forEach(train -> ticketDTOs.add((T) trainMapper.toTrainTicketDto(train, "ua")));
            }

            return Optional.of(ticketDTOs);
        }

        return Optional.empty();
    }

}
