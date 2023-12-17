package com.booking.app.mapper;

import com.booking.app.dto.TicketDTO;
import com.booking.app.entity.Ticket;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TicketMapper {
    public TicketDTO toTicketDTO(Ticket ticket);

    public Ticket toTicket(TicketDTO ticketDTO);
}
