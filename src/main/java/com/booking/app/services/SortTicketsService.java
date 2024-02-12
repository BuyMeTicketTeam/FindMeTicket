package com.booking.app.services;

import com.booking.app.dto.RequestSortedTicketsDTO;
import com.booking.app.dto.BusTicketDTO;

import java.util.List;

public interface SortTicketsService {
    List<BusTicketDTO> getSortedTickets(RequestSortedTicketsDTO requestSortedTicketsDTO, String request);
}
