package com.booking.app.controller;

import com.booking.app.dto.CitiesDTO;
import com.booking.app.dto.StartLettersDTO;
import com.booking.app.services.TypeAheadService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@ExtendWith(MockitoExtension.class)
class TypeAheadControllerTest {
    @InjectMocks
    private TypeAheadController typeAheadController;

    @Mock
    private TypeAheadService typeAheadService;

    @Mock
    private MockHttpServletRequest request;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    List<CitiesDTO> expectedCities;

    @BeforeEach
    void setUp() {
        mockMvc = standaloneSetup(typeAheadController).build();
        objectMapper = new ObjectMapper();
        expectedCities = List.of(
                CitiesDTO.builder().country("UA").cityUa("Дніпро").cityEng("Dnipro").build(),
                CitiesDTO.builder().country("UA").cityUa("Дніпровка").cityEng("Dniprovka").build(),
                CitiesDTO.builder().country("UA").cityUa("Дніпрорудне").cityEng("Dniprorudne").build()
        );
    }

    @Test
    void getCities_citiesResponse_ok() throws Exception {
        request.addHeader(HttpHeaders.CONTENT_LANGUAGE, "en");
        StartLettersDTO startLettersDTO = new StartLettersDTO("Dn");
        when(typeAheadService.findMatches(startLettersDTO, request.getHeader(HttpHeaders.CONTENT_LANGUAGE)))
                .thenReturn(expectedCities);
        MvcResult mvcResult = mockMvc.perform(post("/typeAhead")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(startLettersDTO))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        mvcResult.getResponse().setContentType("application/json;charset=UTF-8");
        String contentAsString = mvcResult.getResponse()
                .getContentAsString();
        List<CitiesDTO> actualCities = objectMapper.readValue(contentAsString, new TypeReference<List<CitiesDTO>>() {
        });

        assertThat(actualCities).hasSize(3);
        assertThat(actualCities).containsExactlyElementsOf(expectedCities);
    }

}
