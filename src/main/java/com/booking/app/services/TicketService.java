package com.booking.app.services;

import com.booking.app.dto.RequestSortedTicketsDto;
import com.booking.app.dto.RequestTicketsDto;
import com.booking.app.dto.TicketDto;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface TicketService {

    /**
     * Retrieves bus OR/AND train tickets based on the provided request.
     *
     * @param dto the request containing filtering criteria
     * @param <T> the type of ticket DTO
     * @return a list of tickets or an empty optional if no tickets are found
     * @throws IOException if an I/O error occurs while determining the language
     */
    <T extends TicketDto> Optional<List<T>> getTickets(RequestTicketsDto dto) throws IOException;

    /**
     * Retrieves and sorts a list of tickets based on the specified criteria.
     *
     * @param dto      an object containing the departure city, arrival city, and departure date.
     * @param language the language in which the ticket information should be returned.
     * @return a sorted list of TicketDto objects according to the specified criteria.
     */
    List<TicketDto> getSortedTickets(RequestSortedTicketsDto dto, String language);

}
