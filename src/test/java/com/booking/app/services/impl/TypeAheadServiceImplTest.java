package com.booking.app.services.impl;

import com.booking.app.dto.CitiesDTO;
import com.booking.app.dto.StartLettersDTO;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TypeAheadServiceImplTest {
    @InjectMocks
    private TypeAheadServiceImpl typeAheadService;

    @Mock
    private UkrPlacesRepository ukrPlacesRepository;

    @Mock
    private LanguageDetectorService languageDetectorService;

    @Mock
    private UkrainianPlacesMapper ukrainianPlacesMapper;

    private StartLettersDTO startLettersDTO;

    @BeforeEach
    void setUp() {
        startLettersDTO = StartLettersDTO.builder().startLetters("Dn").build();
    }

    @Test
    void findMatches_englishStartLettersAndEnglishSite_Success() throws IOException {
        String siteLanguage = "eng";
        String inputLanguage = "eng";

        List<CitiesDTO> placesList = List.of(
                CitiesDTO.builder()
                        .siteLanguage(siteLanguage)
                        .cityEng("Dniprovka")
                        .country("UA")
                        .build(),
                CitiesDTO.builder()
                        .siteLanguage(siteLanguage)
                        .cityEng("Dnipro")
                        .country("UA")
                        .build()
        );
        when(languageDetectorService.detectLanguage(anyString())).thenReturn(Optional.of(inputLanguage));
        when(ukrPlacesRepository.findUkrainianPlacesByNameEngStartsWithIgnoreCaseAndNameEngNotContainingIgnoreCase(startLettersDTO.getStartLetters(), "oblast")).thenReturn(Optional.of(anyList()));
        when(typeAheadService.findMatches(startLettersDTO, siteLanguage)).thenReturn(placesList);
        List<CitiesDTO> result = typeAheadService.findMatches(startLettersDTO, siteLanguage);

        assertEquals(2, result.size());

        assertEquals("UA", result.get(0).getCountry());
        assertEquals(siteLanguage, result.get(0).getSiteLanguage());
        assertEquals("Dniprovka", result.get(0).getCityEng());
        assertNull(result.get(0).getCityUa());

        assertEquals("UA", result.get(1).getCountry());
        assertEquals(siteLanguage, result.get(1).getSiteLanguage());
        assertEquals("Dnipro", result.get(1).getCityEng());
        assertNull(result.get(1).getCityUa());
    }

    @Test
    void findMatches_ukrainianStartLettersAndEnglishSite_Success() throws IOException {
        String siteLanguage = "eng";
        String inputLanguage = "ua";

        List<CitiesDTO> placesList = List.of(
                CitiesDTO.builder()
                        .siteLanguage(siteLanguage)
                        .cityEng("Dniprovka")
                        .cityUa("Дніпровка")
                        .country("UA")
                        .build(),
                CitiesDTO.builder()
                        .siteLanguage(siteLanguage)
                        .cityEng("Dnipro")
                        .cityUa("Дніпро")
                        .country("UA")
                        .build()
        );
        when(languageDetectorService.detectLanguage(anyString())).thenReturn(Optional.of(inputLanguage));
        when(ukrPlacesRepository.findUkrainianPlacesByNameUaStartsWithIgnoreCaseAndNameUaNotContainingIgnoreCase(startLettersDTO.getStartLetters(), "область")).thenReturn(Optional.of(anyList()));
        when(typeAheadService.findMatches(startLettersDTO, siteLanguage)).thenReturn(placesList);
        List<CitiesDTO> result = typeAheadService.findMatches(startLettersDTO, siteLanguage);

        assertEquals(2, result.size());

        assertEquals("UA", result.get(0).getCountry());
        assertEquals(siteLanguage, result.get(0).getSiteLanguage());
        assertEquals("Dniprovka", result.get(0).getCityEng());
        assertEquals("Дніпровка", result.get(0).getCityUa());

        assertEquals("UA", result.get(1).getCountry());
        assertEquals(siteLanguage, result.get(1).getSiteLanguage());
        assertEquals("Dnipro", result.get(1).getCityEng());
        assertEquals("Дніпро", result.get(1).getCityUa());
    }

    @Test
    void findMatches_ukrainianStartLettersAndUkrainianSite_Success() throws IOException {
        String siteLanguage = "ua";
        String inputLanguage = "ua";

        List<CitiesDTO> placesList = List.of(
                CitiesDTO.builder()
                        .siteLanguage(siteLanguage)
                        .cityUa("Дніпровка")
                        .country("UA")
                        .build(),
                CitiesDTO.builder()
                        .siteLanguage(siteLanguage)
                        .cityUa("Дніпро")
                        .country("UA")
                        .build()
        );
        when(languageDetectorService.detectLanguage(anyString())).thenReturn(Optional.of(inputLanguage));
        when(ukrPlacesRepository.findUkrainianPlacesByNameUaStartsWithIgnoreCaseAndNameUaNotContainingIgnoreCase(startLettersDTO.getStartLetters(), "область")).thenReturn(Optional.of(anyList()));
        when(typeAheadService.findMatches(startLettersDTO, siteLanguage)).thenReturn(placesList);
        List<CitiesDTO> result = typeAheadService.findMatches(startLettersDTO, siteLanguage);

        assertEquals(2, result.size());

        assertEquals("UA", result.get(0).getCountry());
        assertEquals(siteLanguage, result.get(0).getSiteLanguage());
        assertEquals("Дніпровка", result.get(0).getCityUa());
        assertNull(result.get(0).getCityEng());

        assertEquals("UA", result.get(1).getCountry());
        assertEquals(siteLanguage, result.get(1).getSiteLanguage());
        assertEquals("Дніпро", result.get(1).getCityUa());
        assertNull(result.get(1).getCityEng());
    }

    @Test
    void findMatches_englishStartLettersAndUkrainianSite_Success() throws IOException {
        String siteLanguage = "ua";
        String inputLanguage = "eng";

        List<CitiesDTO> placesList = List.of(
                CitiesDTO.builder()
                        .siteLanguage(siteLanguage)
                        .cityEng("Dniprovka")
                        .cityUa("Дніпровка")
                        .country("UA")
                        .build(),
                CitiesDTO.builder()
                        .siteLanguage(siteLanguage)
                        .cityEng("Dnipro")
                        .cityUa("Дніпро")
                        .country("UA")
                        .build()
        );
        when(languageDetectorService.detectLanguage(anyString())).thenReturn(Optional.of(inputLanguage));
        when(ukrPlacesRepository.findUkrainianPlacesByNameEngStartsWithIgnoreCaseAndNameEngNotContainingIgnoreCase(startLettersDTO.getStartLetters(), "oblast")).thenReturn(Optional.of(anyList()));
        when(typeAheadService.findMatches(startLettersDTO, siteLanguage)).thenReturn(placesList);
        List<CitiesDTO> result = typeAheadService.findMatches(startLettersDTO, siteLanguage);

        assertEquals(2, result.size());

        assertEquals("UA", result.get(0).getCountry());
        assertEquals(siteLanguage, result.get(0).getSiteLanguage());
        assertEquals("Dniprovka", result.get(0).getCityEng());
        assertEquals("Дніпровка", result.get(0).getCityUa());

        assertEquals("UA", result.get(1).getCountry());
        assertEquals(siteLanguage, result.get(1).getSiteLanguage());
        assertEquals("Dnipro", result.get(1).getCityEng());
        assertEquals("Дніпро", result.get(1).getCityUa());
    }

}