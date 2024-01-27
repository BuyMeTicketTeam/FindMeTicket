package com.booking.app.mapper;

import com.booking.app.dto.TicketDTO;
import com.booking.app.entity.Route;
import com.booking.app.entity.Ticket;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TicketMapper {
    Ticket toEntity(TicketDTO ticketDTO);


    @Mapping(source ="route.departureCity" ,target ="departureCity")
    @Mapping(source ="route.arrivalCity" ,target ="arrivalCity")
    @Mapping(source ="route.departureDate" ,target ="departureDate", qualifiedByName = "departureTimeMapping")
    @Mapping(source ="travelTime" ,target ="travelTime", qualifiedByName = "decimalToString")
    TicketDTO toDto(Ticket ticket);


    @Named("decimalToString")
    public static String decimalToString(BigDecimal travelTime) {

        int hours = travelTime.intValue() / 60;
        int minutes = travelTime.intValue() % 60;

        return String.format("%sгод %sхв", hours, minutes);
    }

    @Named("departureTimeMapping")
    public static String departureTimeMapping(String departureCity){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(departureCity, formatter);
        formatter = DateTimeFormatter.ofPattern("d.MM, E", new Locale("uk"));
        return date.format(formatter);
    }


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Ticket partialUpdate(TicketDTO ticketDTO, @MappingTarget Ticket ticket);
}