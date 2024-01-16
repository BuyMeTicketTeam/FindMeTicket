package com.booking.app.controller;

import com.booking.app.dto.CitiesDTO;
import com.booking.app.dto.RequestTypeAheadDTO;
import com.booking.app.services.TypeAheadService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TypeAheadControllerTest {

    @InjectMocks
    private TypeAheadController typeAheadController;

    @Mock
    private TypeAheadService typeAheadService;

    @Mock
    private MockHttpServletRequest mockHttpServletRequest;

//    @Test
//    void testGetCities() {
//
//        RequestTypeAheadDTO request = RequestTypeAheadDTO.builder().startLetters("Ів").build();
//
//        CitiesDTO city1 = CitiesDTO.builder().cityUkr("Іванівка").cityEng("Іvanivka").build();
//        CitiesDTO city2 = CitiesDTO.builder().cityUkr("Іванов").cityEng("Іvaniv").build();
//
//        List<CitiesDTO> expectedResponse = Arrays.asList(city1, city2);
//
//
//        when(typeAheadService.findMatchesUA(request,"ua")).thenReturn(expectedResponse);
//
//        ResponseEntity<List<CitiesDTO>> response = typeAheadController.getCities(mockHttpServletRequest, request);
//
//
//        assertNotNull(response);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(expectedResponse, response.getBody());
//    }
}
