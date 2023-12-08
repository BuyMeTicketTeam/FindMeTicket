package com.booking.app.services.impl;

import com.booking.app.dto.CitiesDTO;
import com.booking.app.dto.RequestTypeAheadDTO;
import com.booking.app.entity.UkrainianPlaces;
import com.booking.app.mapper.UkrainianPlacesMapper;
import com.booking.app.repositories.UkrPlacesRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TypeAheadServiceImplTest {

    @InjectMocks
    private TypeAheadServiceImpl typeAheadService;

    @Mock
    private UkrPlacesRepository ukrPlacesRepository;

    @Mock
    private UkrainianPlacesMapper ukrainianPlacesMapper;

    @Test
    void testFindMatches() {
        RequestTypeAheadDTO request = RequestTypeAheadDTO.builder().startLetters("Дн").build();

        UkrainianPlaces place1 = UkrainianPlaces.builder()
                .nameUa("Дніпро").nameEng("Dnipro").build();
        UkrainianPlaces place2 = UkrainianPlaces.builder()
                .nameUa("Дніпрорудне").nameEng("Dniprorudne").build();

        List<UkrainianPlaces> placesList = new ArrayList<>();
        placesList.add(place1);
        placesList.add(place2);

        when(ukrPlacesRepository.findUkrainianPlacesByNameUaStartingWithIgnoreCase(request.getStartLetters()))
                .thenReturn(Optional.of(placesList));

        CitiesDTO cityDTO1 = CitiesDTO.builder()
                .cityUkr(place1.getNameUa()).cityEng(place1.getNameEng()).build();
        CitiesDTO cityDTO2 = CitiesDTO.builder()
                .cityUkr(place2.getNameUa()).cityEng(place2.getNameEng()).build();

        when(ukrainianPlacesMapper.ukrainianPlaceToCityDTO(place1)).thenReturn(cityDTO1);
        when(ukrainianPlacesMapper.ukrainianPlaceToCityDTO(place2)).thenReturn(cityDTO2);

        List<CitiesDTO> result = typeAheadService.findMatches(request);

        assertEquals(2, result.size());
        assertEquals("Дніпро", result.get(0).getCityUkr());
        assertEquals("Дніпрорудне", result.get(1).getCityUkr());
    }
}