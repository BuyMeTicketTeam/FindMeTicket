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
import jakarta.el.MethodNotFoundException;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.pool.TypePool;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.ArrayList;
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

        tickets.sort((o1, o2) -> {
            try {
                String get = "get";
                Method getter1 = switch (o1) {
                    case BusTicket t -> BusTicket.class.getMethod(get + dto.getSortingBy());
                    case TrainTicket t -> TrainTicket.class.getMethod(get + dto.getSortingBy());
                    default -> throw new MethodNotFoundException();
                };

                Method getter2 = switch (o2) {
                    case BusTicket t -> BusTicket.class.getMethod(get + dto.getSortingBy());
                    case TrainTicket t -> TrainTicket.class.getMethod(get + dto.getSortingBy());
                    default -> throw new MethodNotFoundException();
                };

                Comparable fieldValue1 = (Comparable) getter1.invoke(o1);
                Comparable fieldValue2 = (Comparable) getter2.invoke(o2);

                int ascending = dto.isAscending() ? 1 : -1;

                return ascending * fieldValue1.compareTo(fieldValue2);
            } catch (Exception e) {
                return 0;
            }
        });

        List<TicketDto> result = new LinkedList<>();

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
                default -> throw new UnsupportedOperationException("Unsupported ticket type: " + ticket.getClass().getSimpleName());
            }

        }

        return result;
    }

}
