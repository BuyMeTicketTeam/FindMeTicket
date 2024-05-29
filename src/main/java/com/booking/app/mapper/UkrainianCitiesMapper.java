package com.booking.app.mapper;

import com.booking.app.dto.CitiesDto;
import com.booking.app.entity.UkrainianCities;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UkrainianCitiesMapper {

    @Mapping(target = "cityUa", source = "nameUa", ignore = true)
    @Mapping(target = "cityEng", source = "nameEng", ignore = true)
    @Mapping(target = "siteLanguage", ignore = true)
    @Mapping(target = "country", source = "country")
    CitiesDto toCitiesDTO(UkrainianCities ukrainianCities);

    default List<CitiesDto> toCitiesDtoList(List<UkrainianCities> ukrainianCitiesList, String inputLanguage, String siteLanguage) {
        return ukrainianCitiesList.stream()
                .map(place -> {
                    CitiesDto citiesDTO = toCitiesDTO(place);
                    citiesDTO.setSiteLanguage(siteLanguage);

                    switch (siteLanguage) {
                        case "ua":
                            switch (inputLanguage) {
                                case "ua":
                                    citiesDTO.setCityUa(place.getNameUa());
                                    break;
                                case "eng":
                                    citiesDTO.setCityEng(place.getNameEng());
                                    citiesDTO.setCityUa(place.getNameUa());
                                    break;
                            }
                            break;

                        case "eng":
                            switch (inputLanguage) {
                                case "eng":
                                    citiesDTO.setCityEng(place.getNameEng());
                                    break;
                                case "ua":
                                    citiesDTO.setCityEng(place.getNameEng());
                                    citiesDTO.setCityUa(place.getNameUa());
                                    break;
                            }
                            break;
                    }

                    return citiesDTO;
                })
                .toList();
    }

}
