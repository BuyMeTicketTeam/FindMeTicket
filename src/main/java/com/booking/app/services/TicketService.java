package com.booking.app.services;

import com.booking.app.dto.BusTicketDTO;
import com.booking.app.dto.RequestSortedTicketsDTO;

import java.util.List;

public interface TicketService {
    List<BusTicketDTO> getBusTickets(RequestSortedTicketsDTO dto);
}
