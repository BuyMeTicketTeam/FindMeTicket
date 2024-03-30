package com.booking.app.mapper;

import com.booking.app.dto.RequestTicketsDTO;
import com.booking.app.entity.Route;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, builder = @Builder(disableBuilder = true))
public interface RouteMapper {

    @Mapping(target = "addingTime", expression = "java(java.time.LocalDateTime.now())")
    Route toRoute(RequestTicketsDTO requestTicketsDTO);

}
