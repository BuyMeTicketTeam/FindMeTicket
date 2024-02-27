package com.booking.app.services.impl;

import com.booking.app.dto.RequestSortedTicketsDTO;
import com.booking.app.dto.TicketDto;
import com.booking.app.entity.BusTicket;
import com.booking.app.entity.Route;
import com.booking.app.entity.Ticket;
import com.booking.app.entity.TrainTicket;
import com.booking.app.mapper.BusMapper;
import com.booking.app.mapper.TrainMapper;
import com.booking.app.repositories.RouteRepository;
import com.booking.app.services.SortTicketsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SortTicketsServiceImpl implements SortTicketsService {

    private final RouteRepository routeRepository;

    private final BusMapper busMapper;

    private final TrainMapper trainMapper;

    public List<TicketDto> getSortedTickets(RequestSortedTicketsDTO requestSortedTicketsDTO, String language) {
        Route route = routeRepository.findByDepartureCityAndArrivalCityAndDepartureDate(requestSortedTicketsDTO.getDepartureCity(), requestSortedTicketsDTO.getArrivalCity(), requestSortedTicketsDTO.getDepartureDate());
        if (Objects.nonNull(route)) {
            List<Ticket> tickets = new ArrayList<>(route.getTickets().stream().toList());

            tickets.sort((o1, o2) -> {
                try {
                    Field field =
//                        switch (o1){
//                            case BusTicket t -> getClass().getDeclaredField(requestSortedTicketsDTO.getSortingBy());
//                            case TrainTicket t -> getClass().getDeclaredField(re);
//                    }
                            o1.getClass().getDeclaredField(requestSortedTicketsDTO.getSortingBy());
                    field.setAccessible(true);

                    Comparable fieldValue1 = (Comparable) field.get(o1);
                    Comparable fieldValue2 = (Comparable) field.get(o2);

                    int ascending = requestSortedTicketsDTO.isAscending() ? 1 : -1;

                    return ascending * fieldValue1.compareTo(fieldValue2);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    return 0;
                }
            });

            List<TicketDto> result = new ArrayList<>();

            for (Ticket ticket : tickets) {
                switch (ticket) {
                    case BusTicket t -> result.add(busMapper.ticketToTicketDto(t, language));
                    case TrainTicket t -> result.add(trainMapper.toTrainTicketDto(t, language));
                    default -> throw new RuntimeException();
                }
            }

            return result;
        }
        return List.of();
    }

}
