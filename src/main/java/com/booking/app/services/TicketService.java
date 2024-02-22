package com.booking.app.services;

import com.booking.app.dto.RequestTicketsDTO;
import com.booking.app.dto.TicketDto;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface TicketService {
    <T extends TicketDto> Optional<List<T>> getBusTickets(RequestTicketsDTO dto) throws IOException;
}
