package com.booking.app.mappers;

import com.booking.app.dto.tickets.ResponseTicketDto;
import com.booking.app.dto.tickets.TrainComfortInfoDTO;
import com.booking.app.entities.ticket.train.TrainInfo;
import com.booking.app.entities.ticket.train.TrainTicket;
import com.booking.app.utils.formatters.CustomDateTimeFormatter;
import com.booking.app.utils.formatters.TravelTimeFormatter;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        builder = @Builder(disableBuilder = true),
        imports = {
                CustomDateTimeFormatter.class,
                TravelTimeFormatter.class
        })
public interface TrainMapper {

    @Mapping(source = "route.departureCity", target = "departureCity")
    @Mapping(source = "route.arrivalCity", target = "arrivalCity")
    @Mapping(source = "id", target = "id")
    @Mapping(target = "departureDate", expression = "java(CustomDateTimeFormatter.formatDate(ticket.getRoute().getDepartureDate(), language))")
    @Mapping(target = "travelTime", expression = "java(TravelTimeFormatter.format(ticket.getTravelTime(), language))")
    @Mapping(source = "carrier", target = "carrier")
    @Mapping(target = "type", constant = "TRAIN")
    ResponseTicketDto toTrainTicketDto(TrainTicket ticket, @Context String language);

    @Mapping(source = "link", target = "url")
    TrainComfortInfoDTO toTrainComfortInfoDTO(TrainInfo ticket);


    @AfterMapping
    default void getPrice(TrainTicket ticket, @MappingTarget ResponseTicketDto ticketDto) {
        ticketDto.setPrice(ticket.getPrice());
    }


}
