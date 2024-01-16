package com.booking.app.services.impl;

import com.booking.app.dto.RequestSortedTicketsDTO;
import com.booking.app.dto.TicketDTO;
import com.booking.app.entity.Route;
import com.booking.app.entity.Ticket;
import com.booking.app.mapper.TicketMapper;
import com.booking.app.repositories.RouteRepository;
import com.booking.app.repositories.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SortedTicketsServiceImpl {
    private final RouteRepository routeRepository;
    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;

    public List<TicketDTO> getSortedTickets(RequestSortedTicketsDTO requestSortedTicketsDTO){

        List<Ticket> tickets = routeRepository.findByDepartureCityAndArrivalCityAndDepartureDate(requestSortedTicketsDTO.getDepartureCity(), requestSortedTicketsDTO.getArrivalCity(), requestSortedTicketsDTO.getDepartureDate()).getTicketList();



        tickets.sort((o1, o2) -> {
            try {
                Field field = o1.getClass().getDeclaredField(requestSortedTicketsDTO.getSortingBy());
                field.setAccessible(true);

                Comparable fieldValue1 = (Comparable) field.get(o1);
                Comparable fieldValue2 = (Comparable) field.get(o2);

                return fieldValue1.compareTo(fieldValue2);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                return 0;
            }
        });

        List<TicketDTO> result = new LinkedList<>();

        for(int i = 0; i<30 && i < tickets.size(); i++){
            result.add(ticketMapper.toTicketDTO(tickets.get(i)));
        }

        return result;
    }
}
