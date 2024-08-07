package com.booking.app.mappers.utils;

import com.booking.app.constants.TransportType;
import com.booking.app.entities.ticket.Ticket;
import com.booking.app.entities.ticket.bus.BusTicket;
import com.booking.app.entities.ticket.train.TrainTicket;
import lombok.experimental.UtilityClass;
import org.mapstruct.Named;

@UtilityClass
public class TicketMapperUtils {

    @Named("typeMapping")
    public static TransportType typeMapping(Ticket ticket) {
        if (ticket instanceof BusTicket) {
            return TransportType.BUS;
        } else if (ticket instanceof TrainTicket) {
            return TransportType.TRAIN;
        }
        return null;
    }
}