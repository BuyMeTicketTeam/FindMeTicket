package com.booking.app.mappers;

import com.booking.app.dto.tickets.ResponseTicketDto;
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

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TicketMapper {

    String PATTERN = "d.MM, E";

    @Named("arrivalTimeFormatter")
    static String arrivalTimeFormatter(Instant arrivalDateTime) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(arrivalDateTime, ZoneId.systemDefault());

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
        return localDateTime.format(timeFormatter);
    }

    @Named("arrivalDateFormatter")
    static String arrivalDateFormatter(Instant arrivalDateTime) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(arrivalDateTime, ZoneId.systemDefault());
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return localDateTime.format(dateFormatter);
    }

    @Named("departureTimeMapping")
    static String departureTimeMapping(String departureDate, @Context String language) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d");
        return switch (language) {
            case ("ua") -> {
                LocalDate date = LocalDate.parse(departureDate, formatter);
                formatter = DateTimeFormatter.ofPattern(PATTERN, new Locale("uk"));
                yield date.format(formatter);
            }
            case ("eng") -> {
                LocalDate date = LocalDate.parse(departureDate, formatter);
                formatter = DateTimeFormatter.ofPattern(PATTERN, new Locale("en"));
                yield date.format(formatter);
            }
            default -> {
                LocalDate date = LocalDate.parse(departureDate, formatter);
                formatter = DateTimeFormatter.ofPattern(PATTERN, new Locale("uk"));
                yield date.format(formatter);
            }
        };
    }

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

    @Mapping(source = "route.departureCity", target = "departureCity")
    @Mapping(source = "route.arrivalCity", target = "arrivalCity")
    @Mapping(source = "route.departureDate", target = "departureDate", qualifiedByName = "departureTimeMapping")
    @Mapping(source = "travelTime", target = "travelTime", qualifiedByName = "decimalToString")
    @Mapping(source = "arrivalDateTime", target = "arrivalDate", qualifiedByName = "arrivalDateFormatter")
    @Mapping(source = "arrivalDateTime", target = "arrivalTime", qualifiedByName = "arrivalTimeFormatter")
    @Named("ticketToTicketDto")
    ResponseTicketDto ticketToTicketDto(Ticket ticket, @Context String language);

    @IterableMapping(qualifiedByName = "ticketToTicketDto")
    List<ResponseTicketDto> toTicketDtoList(List<Ticket> ticketList, @Context String language);


}
