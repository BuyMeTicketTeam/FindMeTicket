package com.booking.app.mapper;

import com.booking.app.dto.CitiesDTO;
import com.booking.app.entity.UkrainianPlaces;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UkrainianPlacesMapper {

    @Mapping(target = "cityUa", source = "nameUa", ignore = true)
    @Mapping(target = "cityEng", source = "nameEng", ignore = true)
    @Mapping(target = "siteLanguage", ignore = true)
    @Mapping(target = "country", source = "country")
    CitiesDTO toCitiesDTO(UkrainianPlaces ukrainianPlaces);

    default List<CitiesDTO> toCitiesDTOList(List<UkrainianPlaces> ukrainianPlacesList, String inputLanguage, String siteLanguage) {

        return ukrainianPlacesList.stream()
                .map(place -> {
                    CitiesDTO citiesDTO = toCitiesDTO(place);
                    citiesDTO.setSiteLanguage(siteLanguage);
                    if (siteLanguage.contains("ua")) {
                        if (inputLanguage.equals("ua")) {
                            citiesDTO.setCityUa(place.getNameUa());
                        }
                        if (inputLanguage.equals("eng")) {
                            citiesDTO.setCityEng(place.getNameEng());
                            citiesDTO.setCityUa(place.getNameUa());
                        }
                    }
                    if (siteLanguage.contains("eng")) {
                        if (inputLanguage.equals("eng")) {
                            citiesDTO.setCityEng(place.getNameEng());
                        }
                        if (inputLanguage.equals("ua")) {
                            citiesDTO.setCityEng(place.getNameEng());
                            citiesDTO.setCityUa(place.getNameUa());
                        }
                    }
                    return citiesDTO;
                })
                .toList();
    }

}
