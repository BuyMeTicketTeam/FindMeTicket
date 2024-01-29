package com.booking.app.services;

import com.booking.app.dto.RequestSortedTicketsDTO;
import com.booking.app.dto.TicketDTO;

import java.util.List;

public interface SortTicketsService {
    List<TicketDTO> getSortedTickets(RequestSortedTicketsDTO requestSortedTicketsDTO);
}
