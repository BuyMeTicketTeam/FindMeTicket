package com.booking.app.mappers;

import com.booking.app.dto.tickets.RequestTicketsDto;
import com.booking.app.entities.ticket.Route;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, builder = @Builder(disableBuilder = true))
public interface RouteMapper {

    @Mapping(target = "addingTime", expression = "java(java.time.Instant.now())")
    Route toRoute(RequestTicketsDto dto);


}
