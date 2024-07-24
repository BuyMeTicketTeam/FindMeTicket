package com.booking.app.controllers;


import com.booking.app.constants.ContentLanguage;
import com.booking.app.constants.SortCriteria;
import com.booking.app.constants.SortType;
import com.booking.app.dto.RequestSortedTicketsDto;
import com.booking.app.entities.ticket.Route;
import com.booking.app.entities.ticket.bus.BusTicket;
import com.booking.app.entities.ticket.train.TrainTicket;
import com.booking.app.mappers.TicketMapper;
import com.booking.app.repositories.RouteRepository;
import com.booking.app.repositories.TicketRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import lombok.SneakyThrows;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureEmbeddedDatabase(provider = AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY)
@ActiveProfiles(profiles = {"test"})
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@DisplayName("Ticket Controller API tests")
@TestPropertySource(properties = {"jwtSecret = 134153634gfdgdfhgdfg    4142145",
        "api.currency-rate-key=42fedgerdg43564356", "app.googleClientId=ffsdfsdgfdfh435e634"})
public class TicketControllerTest {

    private static final String URL = "/tickets";
    private static final String DEPARTURE_CITY = "Dnipro";
    private static final String ARRIVAL_CITY = "Lviv";
    private static final String UA = ContentLanguage.UA.getLanguage();
    private static final String ENG = ContentLanguage.ENG.getLanguage();

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mvc;

    @BeforeAll
    public void setup() {
        routeRepository.deleteAll();
        initData();

        this.mvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    private void initData() {
        List<BusTicket> busTickets = new ArrayList<>();
        List<TrainTicket> trainTickets = new ArrayList<>();
        Route route = Route.builder()
                .arrivalCity(ARRIVAL_CITY)
                .departureCity(DEPARTURE_CITY)
                .addingTime(Instant.now())
                .departureDate(LocalDate.now())
                .build();

        for (int i = 0; i < 50; i++) {
            busTickets.add(BusTicket.builder()
                    .id(UUID.randomUUID())
                    .route(route)
                    .placeAt(RandomStringUtils.randomAlphabetic(20))
                    .placeFrom(RandomStringUtils.randomAlphabetic(20))
                    .arrivalDateTime(Instant.now())
                    .travelTime(RandomUtils.nextInt(10000))
                    .carrier(RandomStringUtils.randomAlphabetic(10))
                    .build());
        }
        for (int i = 0; i < 50; i++) {
            trainTickets.add(TrainTicket.builder()
                    .id(UUID.randomUUID())
                    .route(route)
                    .placeAt(RandomStringUtils.randomAlphabetic(20))
                    .placeFrom(RandomStringUtils.randomAlphabetic(20))
                    .arrivalDateTime(Instant.now())
                    .travelTime(RandomUtils.nextInt(10000))
                    .carrier(RandomStringUtils.randomAlphabetic(10))
                    .build());
        }
        routeRepository.save(route);
        ticketRepository.saveAll(busTickets);
        ticketRepository.saveAll(trainTickets);
    }

    @Nested
    @DisplayName("POST get sorted tickets")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class Post {
        private RequestSortedTicketsDto requestTicketsDto;

        @BeforeAll
        void beforeAll() {
            requestTicketsDto = RequestSortedTicketsDto.builder()
                    .arrivalCity(ARRIVAL_CITY)
                    .departureCity(DEPARTURE_CITY)
                    .departureDate(LocalDate.now())
                    .build();

        }

        @Test
        @SneakyThrows
        @DisplayName("[200] sort without any criteria with UA language")
        void getSortedTicketsUaIs200() {
            mvc.perform(MockMvcRequestBuilders.post(URL + "/sortBy")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(requestTicketsDto))
                            .header(HttpHeaders.CONTENT_LANGUAGE, UA)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(100)))
                    .andExpect(jsonPath("$[*].departureCity").value(everyItem(is(requestTicketsDto.getDepartureCity()))))
                    .andExpect(jsonPath("$[*].arrivalCity").value(everyItem(is(requestTicketsDto.getArrivalCity()))))
                    .andExpect(jsonPath("$[*].departureDate").value(everyItem(is(TicketMapper.departureTimeMapping(requestTicketsDto.getDepartureDate().toString(), UA)))))
                    .andExpect(jsonPath("$[*].id").exists())
                    .andExpect(jsonPath("$[*].type").exists())
                    .andExpect(jsonPath("$[*].placeFrom").exists())
                    .andExpect(jsonPath("$[*].placeAt").exists())
                    .andExpect(jsonPath("$[*].departureTime").exists())
                    .andExpect(jsonPath("$[*].arrivalTime").exists())
                    .andExpect(jsonPath("$[*].arrivalDate").exists())
                    .andExpect(jsonPath("$[*].travelTime").exists())
                    .andExpect(jsonPath("$[*].price").exists())
                    .andExpect(jsonPath("$[*].carrier").exists());
        }

        @Test
        @SneakyThrows
        @DisplayName("[200] sort without any criteria with ENG language")
        void getSortedTicketsEngIs200() {
            mvc.perform(MockMvcRequestBuilders.post(URL + "/sortBy")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(requestTicketsDto))
                            .header(HttpHeaders.CONTENT_LANGUAGE, ENG)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(100)))
                    .andExpect(jsonPath("$[*].departureCity").value(everyItem(is(requestTicketsDto.getDepartureCity()))))
                    .andExpect(jsonPath("$[*].arrivalCity").value(everyItem(is(requestTicketsDto.getArrivalCity()))))
                    .andExpect(jsonPath("$[*].departureDate").value(everyItem(is(TicketMapper.departureTimeMapping(requestTicketsDto.getDepartureDate().toString(), ENG)))))
                    .andExpect(jsonPath("$[*].id").exists())
                    .andExpect(jsonPath("$[*].type").exists())
                    .andExpect(jsonPath("$[*].placeFrom").exists())
                    .andExpect(jsonPath("$[*].placeAt").exists())
                    .andExpect(jsonPath("$[*].departureTime").exists())
                    .andExpect(jsonPath("$[*].arrivalTime").exists())
                    .andExpect(jsonPath("$[*].arrivalDate").exists())
                    .andExpect(jsonPath("$[*].travelTime").exists())
                    .andExpect(jsonPath("$[*].price").exists())
                    .andExpect(jsonPath("$[*].carrier").exists());
        }

        @Test
        @SneakyThrows
        @DisplayName("[200] sort by price in asc order")
        void getSortedTicketsByPriceAscIs200() {
            RequestSortedTicketsDto dto = requestTicketsDto;
            Map<SortCriteria, SortType> sortCriteria = new HashMap<>();
            sortCriteria.put(SortCriteria.PRICE, SortType.ASC);
            dto.setSortingBy(sortCriteria);
            mvc.perform(MockMvcRequestBuilders.post(URL + "/sortBy")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(requestTicketsDto))
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].price").value(Matchers.lessThan("$[99].price")))
                    .andExpect(jsonPath("$", hasSize(100)))
                    .andExpect(jsonPath("$[*].id").exists())
                    .andExpect(jsonPath("$[*].type").exists())
                    .andExpect(jsonPath("$[*].placeFrom").exists())
                    .andExpect(jsonPath("$[*].placeAt").exists())
                    .andExpect(jsonPath("$[*].departureCity").value(requestTicketsDto.getDepartureCity()))
                    .andExpect(jsonPath("$[*].arrivalCity").value(requestTicketsDto.getArrivalCity()))
                    .andExpect(jsonPath("$[*].departureTime").exists())
                    .andExpect(jsonPath("$[*].departureDate").value(requestTicketsDto.getDepartureDate()))
                    .andExpect(jsonPath("$[*].arrivalTime").exists())
                    .andExpect(jsonPath("$[*].arrivalDate").exists())
                    .andExpect(jsonPath("$[*].travelTime").exists())
                    .andExpect(jsonPath("$[*].carrier").exists());

        }

        @Test
        @SneakyThrows
        @DisplayName("[200] sort by price in desc order")
        void getSortedTicketsByPriceDescIs200() {
            RequestSortedTicketsDto dto = requestTicketsDto;
            Map<SortCriteria, SortType> sortCriteria = new HashMap<>();
            sortCriteria.put(SortCriteria.PRICE, SortType.DESC);
            dto.setSortingBy(sortCriteria);
            mvc.perform(MockMvcRequestBuilders.post(URL + "/sortBy")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(requestTicketsDto))
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[99].price").value(Matchers.lessThan("$[0].price")))
                    .andExpect(jsonPath("$", hasSize(100)))
                    .andExpect(jsonPath("$[*].id").exists())
                    .andExpect(jsonPath("$[*].type").exists())
                    .andExpect(jsonPath("$[*].placeFrom").exists())
                    .andExpect(jsonPath("$[*].placeAt").exists())
                    .andExpect(jsonPath("$[*].departureCity").value(requestTicketsDto.getDepartureCity()))
                    .andExpect(jsonPath("$[*].arrivalCity").value(requestTicketsDto.getArrivalCity()))
                    .andExpect(jsonPath("$[*].departureTime").exists())
                    .andExpect(jsonPath("$[*].departureDate").value(requestTicketsDto.getDepartureDate()))
                    .andExpect(jsonPath("$[*].arrivalTime").exists())
                    .andExpect(jsonPath("$[*].arrivalDate").exists())
                    .andExpect(jsonPath("$[*].travelTime").exists())
                    .andExpect(jsonPath("$[*].carrier").exists());

        }

        @Test
        @SneakyThrows
        @DisplayName("[200] sort by arrival time in asc order")
        void getSortedTicketsByArrivalTimeAscIs200() {
            RequestSortedTicketsDto dto = requestTicketsDto;
            Map<SortCriteria, SortType> sortCriteria = new HashMap<>();
            sortCriteria.put(SortCriteria.ARRIVAL_TIME, SortType.ASC);
            dto.setSortingBy(sortCriteria);
            mvc.perform(MockMvcRequestBuilders.post(URL + "/sortBy")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(requestTicketsDto))
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].arrivalTime").value(Matchers.lessThan("$[99].price")))
                    .andExpect(jsonPath("$", hasSize(100)))
                    .andExpect(jsonPath("$[*].id").exists())
                    .andExpect(jsonPath("$[*].type").exists())
                    .andExpect(jsonPath("$[*].placeFrom").exists())
                    .andExpect(jsonPath("$[*].placeAt").exists())
                    .andExpect(jsonPath("$[*].departureCity").value(requestTicketsDto.getDepartureCity()))
                    .andExpect(jsonPath("$[*].arrivalCity").value(requestTicketsDto.getArrivalCity()))
                    .andExpect(jsonPath("$[*].departureTime").exists())
                    .andExpect(jsonPath("$[*].departureDate").value(requestTicketsDto.getDepartureDate()))
                    .andExpect(jsonPath("$[*].arrivalDate").exists())
                    .andExpect(jsonPath("$[*].travelTime").exists())
                    .andExpect(jsonPath("$[*].price").exists())
                    .andExpect(jsonPath("$[*].carrier").exists());

        }

        @Test
        @SneakyThrows
        @DisplayName("[200] sort by arrival time in desc order")
        void getSortedTicketsByArrivalTimeDescIs200() {
            RequestSortedTicketsDto dto = requestTicketsDto;
            Map<SortCriteria, SortType> sortCriteria = new HashMap<>();
            sortCriteria.put(SortCriteria.ARRIVAL_TIME, SortType.DESC);
            dto.setSortingBy(sortCriteria);
            mvc.perform(MockMvcRequestBuilders.post(URL + "/sortBy")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(requestTicketsDto))
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[99].arrivalTime").value(Matchers.lessThan("$[0].price")))
                    .andExpect(jsonPath("$", hasSize(100)))
                    .andExpect(jsonPath("$[*].id").exists())
                    .andExpect(jsonPath("$[*].type").exists())
                    .andExpect(jsonPath("$[*].placeFrom").exists())
                    .andExpect(jsonPath("$[*].placeAt").exists())
                    .andExpect(jsonPath("$[*].departureCity").value(requestTicketsDto.getDepartureCity()))
                    .andExpect(jsonPath("$[*].arrivalCity").value(requestTicketsDto.getArrivalCity()))
                    .andExpect(jsonPath("$[*].departureTime").exists())
                    .andExpect(jsonPath("$[*].departureDate").value(requestTicketsDto.getDepartureDate()))
                    .andExpect(jsonPath("$[*].arrivalDate").exists())
                    .andExpect(jsonPath("$[*].travelTime").exists())
                    .andExpect(jsonPath("$[*].price").exists())
                    .andExpect(jsonPath("$[*].carrier").exists());

        }

        @Test
        @SneakyThrows
        @DisplayName("[200] sort by departure time in asc order")
        void getSortedTicketsByDepartureTimeAscIs200() {
            RequestSortedTicketsDto dto = requestTicketsDto;
            Map<SortCriteria, SortType> sortCriteria = new HashMap<>();
            sortCriteria.put(SortCriteria.DEPARTURE_TIME, SortType.ASC);
            dto.setSortingBy(sortCriteria);
            mvc.perform(MockMvcRequestBuilders.post(URL + "/sortBy")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(requestTicketsDto))
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].departureTime").value(Matchers.lessThan("$[99].departureTime")))
                    .andExpect(jsonPath("$", hasSize(100)))
                    .andExpect(jsonPath("$[*].id").exists())
                    .andExpect(jsonPath("$[*].type").exists())
                    .andExpect(jsonPath("$[*].placeFrom").exists())
                    .andExpect(jsonPath("$[*].placeAt").exists())
                    .andExpect(jsonPath("$[*].departureCity").value(requestTicketsDto.getDepartureCity()))
                    .andExpect(jsonPath("$[*].arrivalCity").value(requestTicketsDto.getArrivalCity()))
                    .andExpect(jsonPath("$[*].departureDate").value(requestTicketsDto.getDepartureDate()))
                    .andExpect(jsonPath("$[*].arrivalTime").exists())
                    .andExpect(jsonPath("$[*].arrivalDate").exists())
                    .andExpect(jsonPath("$[*].travelTime").exists())
                    .andExpect(jsonPath("$[*].price").exists())
                    .andExpect(jsonPath("$[*].carrier").exists());

        }

        @Test
        @SneakyThrows
        @DisplayName("[200] sort by departure time in desc order")
        void getSortedTicketsByDepartureTimeDescIs200() {
            RequestSortedTicketsDto dto = requestTicketsDto;
            Map<SortCriteria, SortType> sortCriteria = new HashMap<>();
            sortCriteria.put(SortCriteria.DEPARTURE_TIME, SortType.DESC);
            dto.setSortingBy(sortCriteria);
            mvc.perform(MockMvcRequestBuilders.post(URL + "/sortBy")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(requestTicketsDto))
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[99].departureTime").value(Matchers.lessThan("$[0].departureTime")))
                    .andExpect(jsonPath("$", hasSize(100)))
                    .andExpect(jsonPath("$[*].id").exists())
                    .andExpect(jsonPath("$[*].type").exists())
                    .andExpect(jsonPath("$[*].placeFrom").exists())
                    .andExpect(jsonPath("$[*].placeAt").exists())
                    .andExpect(jsonPath("$[*].departureCity").value(requestTicketsDto.getDepartureCity()))
                    .andExpect(jsonPath("$[*].arrivalCity").value(requestTicketsDto.getArrivalCity()))
                    .andExpect(jsonPath("$[*].departureDate").value(requestTicketsDto.getDepartureDate()))
                    .andExpect(jsonPath("$[*].arrivalTime").exists())
                    .andExpect(jsonPath("$[*].arrivalDate").exists())
                    .andExpect(jsonPath("$[*].travelTime").exists())
                    .andExpect(jsonPath("$[*].price").exists())
                    .andExpect(jsonPath("$[*].carrier").exists());

        }

        @Test
        @SneakyThrows
        @DisplayName("[200] sort by travel time in asc order")
        void getSortedTicketsByTravelTimeAscIs200() {
            RequestSortedTicketsDto dto = requestTicketsDto;
            Map<SortCriteria, SortType> sortCriteria = new HashMap<>();
            sortCriteria.put(SortCriteria.TRAVEL_TIME, SortType.ASC);
            dto.setSortingBy(sortCriteria);
            mvc.perform(MockMvcRequestBuilders.post(URL + "/sortBy")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(requestTicketsDto))
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].travelTime").value(Matchers.lessThan("$[99].travelTime")))
                    .andExpect(jsonPath("$", hasSize(100)))
                    .andExpect(jsonPath("$[*].id").exists())
                    .andExpect(jsonPath("$[*].type").exists())
                    .andExpect(jsonPath("$[*].placeFrom").exists())
                    .andExpect(jsonPath("$[*].placeAt").exists())
                    .andExpect(jsonPath("$[*].departureCity").value(requestTicketsDto.getDepartureCity()))
                    .andExpect(jsonPath("$[*].arrivalCity").value(requestTicketsDto.getArrivalCity()))
                    .andExpect(jsonPath("$[*].departureDate").value(requestTicketsDto.getDepartureDate()))
                    .andExpect(jsonPath("$[*].departureTime").exists())
                    .andExpect(jsonPath("$[*].arrivalDate").exists())
                    .andExpect(jsonPath("$[*].arrivalTime").exists())
                    .andExpect(jsonPath("$[*].price").exists())
                    .andExpect(jsonPath("$[*].carrier").exists());

        }

        @Test
        @SneakyThrows
        @DisplayName("[200] sort by travel time in desc order")
        void getSortedTicketsByTravelTimeDescIs200() {
            RequestSortedTicketsDto dto = requestTicketsDto;
            Map<SortCriteria, SortType> sortCriteria = new HashMap<>();
            sortCriteria.put(SortCriteria.ARRIVAL_TIME, SortType.DESC);
            dto.setSortingBy(sortCriteria);
            mvc.perform(MockMvcRequestBuilders.post(URL + "/sortBy")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(requestTicketsDto))
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[99].travelTime").value(Matchers.lessThan("$[0].travelTime")))
                    .andExpect(jsonPath("$", hasSize(100)))
                    .andExpect(jsonPath("$[*].id").exists())
                    .andExpect(jsonPath("$[*].type").exists())
                    .andExpect(jsonPath("$[*].placeFrom").exists())
                    .andExpect(jsonPath("$[*].placeAt").exists())
                    .andExpect(jsonPath("$[*].departureCity").value(requestTicketsDto.getDepartureCity()))
                    .andExpect(jsonPath("$[*].arrivalCity").value(requestTicketsDto.getArrivalCity()))
                    .andExpect(jsonPath("$[*].departureDate").value(requestTicketsDto.getDepartureDate()))
                    .andExpect(jsonPath("$[*].departureTime").exists())
                    .andExpect(jsonPath("$[*].arrivalDate").exists())
                    .andExpect(jsonPath("$[*].arrivalTime").exists())
                    .andExpect(jsonPath("$[*].price").exists())
                    .andExpect(jsonPath("$[*].carrier").exists());

        }
    }

}
