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

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SortTicketsServiceImpl implements SortTicketsService {

    private final RouteRepository routeRepository;

    private final BusMapper busMapper;

    private final TrainMapper trainMapper;

    public List<TicketDto> getSortedTickets(RequestSortedTicketsDTO requestSortedTicketsDTO, String language) {
        List<Ticket> tickets = new java.util.ArrayList<>(routeRepository.findByDepartureCityAndArrivalCityAndDepartureDate(requestSortedTicketsDTO.getDepartureCity(), requestSortedTicketsDTO.getArrivalCity(), requestSortedTicketsDTO.getDepartureDate()).getTickets().stream().toList());

        tickets.sort((o1, o2) -> {
            try {

                Method getter1 = switch (o1) {
                    case BusTicket t -> BusTicket.class.getMethod("get" + requestSortedTicketsDTO.getSortingBy());
                    case TrainTicket t -> TrainTicket.class.getMethod("get" + requestSortedTicketsDTO.getSortingBy());
                    default -> null;
                };

                Method getter2 = switch (o2) {
                    case BusTicket t -> BusTicket.class.getMethod("get" + requestSortedTicketsDTO.getSortingBy());
                    case TrainTicket t -> TrainTicket.class.getMethod("get" + requestSortedTicketsDTO.getSortingBy());
                    default -> null;
                };

                Comparable fieldValue1 = (Comparable) getter1.invoke(o1);
                Comparable fieldValue2 = (Comparable) getter2.invoke(o2);

                int ascending = requestSortedTicketsDTO.isAscending() ? 1 : -1;

                return ascending * fieldValue1.compareTo(fieldValue2);
            } catch (Exception e) {
                return 0;
            }


        });

        List<TicketDto> result = new LinkedList<>();

        for (int i = 0; i < 30 && i < tickets.size(); i++) {

            TicketDto ticketDto = switch (tickets.get(i)) {
                case BusTicket t -> busMapper.ticketToTicketDto(t, language);
                case TrainTicket t -> trainMapper.toTrainTicketDto(t, language);
                default -> null;
            };

            result.add(ticketDto);
        }

        return result;
    }

}
