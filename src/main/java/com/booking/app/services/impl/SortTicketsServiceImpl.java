package com.booking.app.services.impl;

import com.booking.app.dto.RequestSortedTicketsDTO;
import com.booking.app.dto.BusTicketDTO;
import com.booking.app.entity.Ticket;
import com.booking.app.mapper.TicketMapper;
import com.booking.app.repositories.RouteRepository;
import com.booking.app.services.SortTicketsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SortTicketsServiceImpl implements SortTicketsService {

    private final RouteRepository routeRepository;

    private final TicketMapper ticketMapper;

    public List<BusTicketDTO> getSortedTickets(RequestSortedTicketsDTO requestSortedTicketsDTO, String language) {
        List<Ticket> tickets = routeRepository.findByDepartureCityAndArrivalCityAndDepartureDate(requestSortedTicketsDTO.getDepartureCity(), requestSortedTicketsDTO.getArrivalCity(), requestSortedTicketsDTO.getDepartureDate()).getTickets().stream().toList();

        tickets.sort((o1, o2) -> {
            try {
                Field field = o1.getClass().getDeclaredField(requestSortedTicketsDTO.getSortingBy());
                field.setAccessible(true);

                Comparable fieldValue1 = (Comparable) field.get(o1);
                Comparable fieldValue2 = (Comparable) field.get(o2);

                int ascending = requestSortedTicketsDTO.isAscending() ? 1 : -1;

                return ascending * fieldValue1.compareTo(fieldValue2);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                return 0;
            }
        });

        List<BusTicketDTO> result = new LinkedList<>();

        for (int i = 0; i < 30 && i < tickets.size(); i++) {
            result.add(ticketMapper.ticketToTicketDto(tickets.get(i), language));
        }

        return result;
    }

}
