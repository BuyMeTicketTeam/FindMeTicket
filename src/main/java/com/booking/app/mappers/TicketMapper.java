package com.booking.app.mappers;

import com.booking.app.dto.tickets.ResponseTicketDto;
import com.booking.app.entities.ticket.Ticket;
import com.booking.app.mappers.utils.TicketMapperUtils;
import com.booking.app.utils.formatters.CustomDateTimeFormatter;
import com.booking.app.utils.formatters.TravelTimeFormatter;
import org.mapstruct.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        imports = {
                CustomDateTimeFormatter.class,
                TravelTimeFormatter.class,
                ZoneId.class,
                LocalDate.class
        },
        uses = TicketMapperUtils.class)
public interface TicketMapper {

    @Mapping(source = "route.departureCity", target = "departureCity")
    @Mapping(source = "route.arrivalCity", target = "arrivalCity")
    @Mapping(source = "ticket", target = "type", qualifiedByName = "typeMapping")
    @Mapping(target = "departureDate", expression = "java(CustomDateTimeFormatter.formatDate(ticket.getRoute().getDepartureDate(), language))")
    @Mapping(target = "travelTime", expression = "java(TravelTimeFormatter.format(ticket.getTravelTime(), language))")
    @Mapping(target = "arrivalDate", expression = "java(CustomDateTimeFormatter.formatDate(LocalDate.ofInstant( ticket.getArrivalDateTime(), ZoneId.systemDefault()), language))")
    @Mapping(target = "arrivalTime", expression = "java(CustomDateTimeFormatter.formatTime(ticket.getArrivalDateTime()))")
    @Named("ticketToTicketDto")
    ResponseTicketDto ticketToTicketDto(Ticket ticket, @Context String language);

    @IterableMapping(qualifiedByName = "ticketToTicketDto")
    List<ResponseTicketDto> toTicketDtoList(List<Ticket> ticketList, @Context String language);

}
