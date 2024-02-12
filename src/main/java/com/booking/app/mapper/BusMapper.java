package com.booking.app.mapper;

import com.booking.app.dto.BusTicketDTO;
import com.booking.app.entity.BusTicket;
import com.booking.app.entity.Ticket;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BusMapper{

    @Mapping(source = "route.departureCity", target = "departureCity")
    @Mapping(source = "route.arrivalCity", target = "arrivalCity")
    @Mapping(source = "route.departureDate", target = "departureDate", qualifiedByName = "departureTimeMapping")
    @Mapping(source = "travelTime", target = "travelTime", qualifiedByName = "decimalToString")
    BusTicketDTO ticketToTicketDto(BusTicket ticket, @Context String language);

    @Named("decimalToString")
    static String decimalToString(BigDecimal travelTime, @Context String language) {
        int hours = travelTime.intValue() / 60;
        int minutes = travelTime.intValue() % 60;
        return switch (language) {
            case ("ua") -> String.format("%sгод %sхв", hours, minutes);
            case ("eng") -> String.format("%sh %smin", hours, minutes);
            default -> String.format("%sгод %sхв", hours, minutes);
        };
    }

    @Named("departureTimeMapping")
    static String departureTimeMapping(String departureDate, @Context String language) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d");
        return switch (language) {
            case ("ua") -> {
                LocalDate date = LocalDate.parse(departureDate, formatter);
                formatter = DateTimeFormatter.ofPattern("d.MM, E", new Locale("uk"));
                yield date.format(formatter);
            }
            case ("eng") -> {
                LocalDate date = LocalDate.parse(departureDate, formatter);
                formatter = DateTimeFormatter.ofPattern("d.MM, E", new Locale("en"));
                yield date.format(formatter);
            }
            default -> {
                LocalDate date = LocalDate.parse(departureDate, formatter);
                formatter = DateTimeFormatter.ofPattern("d.MM, E", new Locale("uk"));
                yield date.format(formatter);
            }
        };
    }

}
