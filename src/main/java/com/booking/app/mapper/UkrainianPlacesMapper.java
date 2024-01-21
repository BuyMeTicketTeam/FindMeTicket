package com.booking.app.mapper;

import com.booking.app.dto.CitiesDTO;
import com.booking.app.entity.UkrainianPlaces;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UkrainianPlacesMapper {

    @Mapping(target = "cityUa",source = "nameUa")
    @Mapping(target = "cityEng",source = "nameEng")
    CitiesDTO toCitiesDTO(UkrainianPlaces ukrainianPlaces);

}
