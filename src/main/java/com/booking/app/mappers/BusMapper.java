package com.booking.app.mappers;

import com.booking.app.dto.tickets.ResponseTicketDto;
import com.booking.app.entities.ticket.bus.BusTicket;
import com.booking.app.utils.formatters.CustomDateTimeFormatter;
import com.booking.app.utils.formatters.TravelTimeFormatter;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        builder = @Builder(disableBuilder = true),
        imports = {
                CustomDateTimeFormatter.class,
                TravelTimeFormatter.class
        })
public interface BusMapper {

    @Mapping(source = "route.departureCity", target = "departureCity")
    @Mapping(source = "route.arrivalCity", target = "arrivalCity")
    @Mapping(target = "departureDate", expression = "java(CustomDateTimeFormatter.formatDate(ticket.getRoute().getDepartureDate(), language))")
    @Mapping(target = "travelTime", expression = "java(TravelTimeFormatter.format(ticket.getTravelTime(), language))")
    @Mapping(target = "type", constant = "BUS")
    ResponseTicketDto ticketToTicketDto(BusTicket ticket, @Context String language);

    @AfterMapping
    default void getPrice(BusTicket ticket, @MappingTarget ResponseTicketDto ticketDto) {
        ticketDto.setPrice(ticket.getPrice());
    }

}
