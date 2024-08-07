package com.booking.app.mappers;

import com.booking.app.constants.TransportType;
import com.booking.app.dto.users.HistoryDto;
import com.booking.app.entities.user.SearchHistory;
import com.booking.app.mappers.model.ArrivalCity;
import com.booking.app.mappers.model.DepartureCity;
import org.mapstruct.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        imports = {
                TransportType.class,
                Arrays.class
        }
)
public interface SearchHistoryMapper {

    @Mapping(source = "departureDate", target = "departureDate")
    @Mapping(source = "addingTime", target = "addingTime", qualifiedByName = "timeToString")
    @Mapping(source = "departureCityId", target = "departureCity", qualifiedByName = "getDepartureCity")
    @Mapping(source = "arrivalCityId", target = "arrivalCity", qualifiedByName = "getArrivalCity")
    @Mapping(target = "bus", expression = "java(Arrays.stream(searchHistory.getTypeTransport()).anyMatch(type -> type.equals(TransportType.BUS)))")
    @Mapping(target = "train", expression = "java(Arrays.stream(searchHistory.getTypeTransport()).anyMatch(type -> type.equals(TransportType.TRAIN)))")
    @Mapping(target = "airplane", expression = "java(Arrays.stream(searchHistory.getTypeTransport()).anyMatch(type -> type.equals(TransportType.AIRPLANE)))")
    @Mapping(target = "ferry", expression = "java(Arrays.stream(searchHistory.getTypeTransport()).anyMatch(type -> type.equals(TransportType.FERRY)))")
    HistoryDto historyToDto(SearchHistory searchHistory, @Context DepartureCity departureCity, @Context ArrivalCity arrivalCity);

    @Named("timeToString")
    static String timeToString(LocalDateTime time) {
        return time.format(DateTimeFormatter.ofPattern("hh:mm a, dd.MM.yyyy"));
    }

    @Named("getDepartureCity")
    default String getDepartureCity(Long departureCityId, @Context DepartureCity departureCity) {
        return departureCity.name();
    }

    @Named("getArrivalCity")
    default String getArrivalCity(Long arrivalCityId, @Context ArrivalCity arrivalCity) {
        return arrivalCity.name();
    }

}
