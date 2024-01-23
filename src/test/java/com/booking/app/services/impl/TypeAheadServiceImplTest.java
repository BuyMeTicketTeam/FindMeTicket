package com.booking.app.services.impl;

import com.booking.app.dto.CitiesDTO;
import com.booking.app.dto.RequestTypeAheadDTO;
import com.booking.app.dto.StartLettersDTO;
import com.booking.app.entity.UkrainianPlaces;
import com.booking.app.mapper.UkrainianPlacesMapper;
import com.booking.app.repositories.UkrPlacesRepository;
import com.booking.app.services.LanguageDetectorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
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
    private LanguageDetectorService languageDetectorService;


    @BeforeEach
    void setUp() {

    }

    @Test
    void findMatchesUA_Success() throws IOException {
        // Arrange
        StartLettersDTO startLettersDTO = new StartLettersDTO("Dn");
        String siteLanguage = "ua";

        when(languageDetectorService.detectLanguage("en")).thenReturn(Optional.of("eng"));

        UkrainianPlaces place1 = UkrainianPlaces.builder()
                .nameEng("Dnipro")
                .country("Country")
                .build(); 6
        UkrainianPlaces place2 = UkrainianPlaces.builder()
                .nameEng("Dniprovka")
                .country("Country")
                .build();
        List<UkrainianPlaces> placesList = List.of(place1, place2);

        when(ukrPlacesRepository.findUkrainianPlacesByNameEngStartsWithIgnoreCaseAndNameEngNotContaining(eq("Dn"), anyString()))
                .thenReturn(Optional.of(placesList));

        // Act
        List<CitiesDTO> result = typeAheadService.findMatchesUA(startLettersDTO, siteLanguage);

        // Assert
        assertEquals(2, result.size());
        assertEquals("Country", result.get(0).getCountry());
        assertEquals("Dnipro", result.get(0).getCityUa());
        assertEquals("Dnipro", result.get(0).getCityEng());
        assertEquals("Country", result.get(1).getCountry());
        assertEquals("Dniprovka", result.get(1).getCityUa());
        assertEquals("Dniprovka", result.get(1).getCityEng());

        // Verify interactions with mocks
        verify(languageDetectorService, times(1)).detectLanguage("Dn");
        verify(ukrPlacesRepository, times(1)).findUkrainianPlacesByNameEngStartsWithIgnoreCaseAndNameEngNotContaining(eq("Dn"), anyString());
    }
}