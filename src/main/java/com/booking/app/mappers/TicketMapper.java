package com.booking.app.mappers;

import com.booking.app.constants.DatePatternsConstants;
import com.booking.app.dto.TicketDto;
import com.booking.app.entities.ticket.Ticket;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        imports = {LocalDateTime.class, ZoneId.class, DateTimeFormatter.class})
public interface TicketMapper {


    @Named("arrivalDateFormatter")
    static String arrivalDateFormatter(Instant arrivalDateTime, @Context String language) {
        String pattern = DatePatternsConstants.D_MM_E;
        LocalDateTime localDateTime = LocalDateTime.ofInstant(arrivalDateTime, ZoneId.systemDefault());

        DateTimeFormatter formatter = switch (language) {
            case "ua" -> DateTimeFormatter.ofPattern(pattern, new Locale("uk"));
            case "eng" -> DateTimeFormatter.ofPattern(pattern, new Locale("en"));
            default -> DateTimeFormatter.ofPattern(pattern, new Locale("uk"));
        };

        return localDateTime.format(formatter);
    }

    @Named("departureDateFormatter")
    static String departureDateFormatter(String departureDate, @Context String language) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d");
        String pattern = DatePatternsConstants.D_MM_E;
        return switch (language) {
            case ("ua") -> {
                LocalDate date = LocalDate.parse(departureDate, formatter);
                formatter = DateTimeFormatter.ofPattern(pattern, new Locale("uk"));
                yield date.format(formatter);
            }
            case ("eng") -> {
                LocalDate date = LocalDate.parse(departureDate, formatter);
                formatter = DateTimeFormatter.ofPattern(pattern, new Locale("en"));
                yield date.format(formatter);
            }
            default -> {
                LocalDate date = LocalDate.parse(departureDate, formatter);
                formatter = DateTimeFormatter.ofPattern(pattern, new Locale("uk"));
                yield date.format(formatter);
            }
        };
    }

    @Mapping(source = "route.departureCity", target = "departureCity")
    @Mapping(source = "route.arrivalCity", target = "arrivalCity")
    @Mapping(source = "route.departureDate", target = "departureDate", qualifiedByName = "departureDateFormatter")
//    @Mapping(source = "travelTime", target = "travelTime", qualifiedByName = "decimalToString")
    @Mapping(source = "arrivalDateTime", target = "arrivalDate", qualifiedByName = "arrivalDateFormatter")
    @Mapping(target = "arrivalTime", expression = "java(LocalDateTime.ofInstant(ticket.getArrivalDateTime(), ZoneId.systemDefault()).toLocalTime().withSecond(0).withNano(0))")
    @Mapping(target = "departureTime", expression = "java(ticket.getDepartureTime().withSecond(0).withNano(0))")
    @Named("ticketToTicketDto")
    TicketDto ticketToTicketDto(Ticket ticket, @Context String language);

    @IterableMapping(qualifiedByName = "ticketToTicketDto")
    List<TicketDto> toTicketDtoList(List<Ticket> ticketList, @Context String language);

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


}
