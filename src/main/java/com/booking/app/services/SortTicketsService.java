package com.booking.app.services;

import com.booking.app.dto.RequestSortedTicketsDTO;
import com.booking.app.dto.BusTicketDTO;
import com.booking.app.dto.TicketDto;

import java.util.List;

public interface SortTicketsService {
    List<TicketDto> getSortedTickets(RequestSortedTicketsDTO requestSortedTicketsDTO, String request);
}
