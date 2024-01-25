package com.booking.app.mapper;

import com.booking.app.dto.TicketDTO;
import com.booking.app.entity.Route;
import com.booking.app.entity.Ticket;
import org.mapstruct.*;

import java.math.BigDecimal;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TicketMapper {
    Ticket toEntity(TicketDTO ticketDTO);


    @Mapping(source ="route.departureCity" ,target ="departureCity")
    @Mapping(source ="route.arrivalCity" ,target ="arrivalCity")
    @Mapping(source ="route.departureDate" ,target ="departureDate")
    @Mapping(source ="travelTime" ,target ="travelTime", qualifiedByName = "decimalToString")
    TicketDTO toDto(Ticket ticket);


    @Named("decimalToString")
    public static String decimalToString(BigDecimal travelTime) {

        int hours = travelTime.intValue() / 60;
        int minutes = travelTime.intValue() % 60;

        return String.format("%sгод %sхв", hours, minutes);
    }


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Ticket partialUpdate(TicketDTO ticketDTO, @MappingTarget Ticket ticket);
}