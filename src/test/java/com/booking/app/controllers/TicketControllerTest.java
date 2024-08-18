package com.booking.app.controllers;


import com.booking.app.constants.ContentLanguage;
import com.booking.app.constants.SortCriteria;
import com.booking.app.constants.SortType;
import com.booking.app.controllers.testUtils.DateTimeUtils;
import com.booking.app.controllers.testUtils.TicketUtils;
import com.booking.app.dto.tickets.RequestSortedTicketsDto;
import com.booking.app.entities.ticket.Route;
import com.booking.app.entities.ticket.bus.BusInfo;
import com.booking.app.entities.ticket.bus.BusTicket;
import com.booking.app.entities.ticket.train.TrainInfo;
import com.booking.app.entities.ticket.train.TrainTicket;
import com.booking.app.repositories.BusTicketRepository;
import com.booking.app.repositories.RouteRepository;
import com.booking.app.repositories.TrainTicketRepository;
import com.booking.app.utils.formatters.CustomDateTimeFormatter;
import com.booking.app.utils.formatters.TravelTimeFormatter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureEmbeddedDatabase(provider = AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY)
@ActiveProfiles(profiles = {"test"})
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@DisplayName("Ticket Controller API tests")
public class TicketControllerTest {

    private static final String URL = "/tickets";
    private static final String DEPARTURE_CITY = "Dnipro";
    private static final String ARRIVAL_CITY = "Lviv";
    private static final int NUMBER_OF_TICKETS = 100;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private BusTicketRepository busTicketRepository;

    @Autowired
    private TrainTicketRepository trainTicketRepository;

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
    }

    private void initData() {
        List<BusTicket> busTickets = new ArrayList<>();
        List<TrainTicket> trainTickets = new ArrayList<>();

        LocalDate departureDate = LocalDate.now();

        Route route = Route.builder()
                .arrivalCity(ARRIVAL_CITY)
                .departureCity(DEPARTURE_CITY)
                .addingTime(Instant.now())
                .departureDate(departureDate)
                .build();

        for (int i = 0; i < NUMBER_OF_TICKETS / 2; i++) {
            LocalTime departureTime = DateTimeUtils.between(LocalTime.MIN, LocalTime.MAX.minus(Duration.ofHours(10)));
            Instant arrivalDateTime = DateTimeUtils.getRandomInstantWithinThreeDays();

            BusTicket busTicket = TicketUtils.generateMockBusTicket(
                    route,
                    departureTime,
                    arrivalDateTime,
                    departureDate
            );
            List<BusInfo> busInfos = TicketUtils.generateBusInfo(busTicket);
            busTicket.setInfoList(busInfos);
            busTickets.add(busTicket);
        }

        for (int i = 0; i < NUMBER_OF_TICKETS / 2; i++) {
            LocalTime departureTime = DateTimeUtils.between(LocalTime.MIN, LocalTime.MAX.minus(Duration.ofHours(10)));
            Instant arrivalDateTime = DateTimeUtils.getRandomInstantWithinThreeDays();

            TrainTicket trainTicket = TicketUtils.generateMockTrainTicket(
                    route,
                    departureTime,
                    arrivalDateTime,
                    departureDate
            );
            List<TrainInfo> trainInfos = TicketUtils.generateTrainInfo(trainTicket);
            trainTicket.setInfoList(trainInfos);
            trainTickets.add(trainTicket);
        }

        routeRepository.save(route);

        busTicketRepository.saveAll(busTickets);
        trainTicketRepository.saveAll(trainTickets);
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

        @SneakyThrows
        private boolean checkSort(ContentLanguage language, SortCriteria criteria, SortType sortType, MvcResult mvnResult) {
            String contentAsString = mvnResult.getResponse().getContentAsString();
            JsonNode jsonNode = objectMapper.readTree(contentAsString);

            switch (criteria) {
                case ARRIVAL_TIME:
                case DEPARTURE_TIME: {
                    LocalTime timeFirst = LocalTime.parse(jsonNode.get(0).get(criteria.getCriteria()).asText());
                    LocalTime timeLast = LocalTime.parse(jsonNode.get(NUMBER_OF_TICKETS - 1).get(criteria.getCriteria()).asText());
                    return (sortType == SortType.ASC) ?
                            timeFirst.isBefore(timeLast) : timeFirst.isAfter(timeLast);
                }
                case TRAVEL_TIME: {
                    Duration travelFirst = TravelTimeFormatter.parse(jsonNode.get(0).get(criteria.getCriteria()).asText(), language);
                    Duration travelLast = TravelTimeFormatter.parse(jsonNode.get(NUMBER_OF_TICKETS - 1).get(criteria.getCriteria()).asText(), language);
                    return (sortType == SortType.ASC) ?
                            travelFirst.compareTo(travelLast) > 0 : travelFirst.compareTo(travelLast) < 0;
                }
                default:
                    throw new IllegalArgumentException("Unsupported sort criteria: " + criteria);
            }
        }

        @Test
        @SneakyThrows
        @DisplayName("[200] sort without any criteria with UA language")
        void getSortedTicketsUaIs200() {
            ContentLanguage language = ContentLanguage.UA;
            RequestSortedTicketsDto requestBody = requestTicketsDto;
            String departureDate = CustomDateTimeFormatter.formatDate(requestBody.getDepartureDate(), language.getLanguage());

            mvc.perform(MockMvcRequestBuilders.post(URL + "/sortBy")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(requestTicketsDto))
                            .header(HttpHeaders.CONTENT_LANGUAGE, language).characterEncoding(StandardCharsets.UTF_8)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(NUMBER_OF_TICKETS)))
                    .andExpect(jsonPath("$[*].departureCity").value(everyItem(is(requestBody.getDepartureCity()))))
                    .andExpect(jsonPath("$[*].arrivalCity").value(everyItem(is(requestBody.getArrivalCity()))))
                    .andExpect(jsonPath("$[*].departureDate").value(everyItem(is(departureDate))))
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
            ContentLanguage language = ContentLanguage.ENG;
            RequestSortedTicketsDto requestBody = requestTicketsDto;
            String departureDate = CustomDateTimeFormatter.formatDate(requestBody.getDepartureDate(), language.getLanguage());

            mvc.perform(MockMvcRequestBuilders.post(URL + "/sortBy")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(requestTicketsDto))
                            .header(HttpHeaders.CONTENT_LANGUAGE, language).characterEncoding(StandardCharsets.UTF_8)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(NUMBER_OF_TICKETS)))
                    .andExpect(jsonPath("$[*].departureCity").value(everyItem(is(requestBody.getDepartureCity()))))
                    .andExpect(jsonPath("$[*].arrivalCity").value(everyItem(is(requestBody.getArrivalCity()))))
                    .andExpect(jsonPath("$[*].departureDate").value(everyItem(is(departureDate))))
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
        @DisplayName("[200] sort by price in asc order with UA language")
        void getSortedTicketsByPriceAscUaIs200() {
            ContentLanguage language = ContentLanguage.UA;
            SortCriteria criteria = SortCriteria.PRICE;
            SortType sortType = SortType.ASC;

            RequestSortedTicketsDto requestBody = requestTicketsDto;
            requestBody.setSortingBy(Map.of(criteria, sortType));

            String departureDate = CustomDateTimeFormatter.formatDate(requestBody.getDepartureDate(), language.getLanguage());

            MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(URL + "/sortBy")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(requestTicketsDto))
                            .header(HttpHeaders.CONTENT_LANGUAGE, language).characterEncoding(StandardCharsets.UTF_8)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(NUMBER_OF_TICKETS)))
                    .andExpect(jsonPath("$[*].id").exists())
                    .andExpect(jsonPath("$[*].type").exists())
                    .andExpect(jsonPath("$[*].placeFrom").exists())
                    .andExpect(jsonPath("$[*].placeAt").exists())
                    .andExpect(jsonPath("$[*].departureCity").value(everyItem(is(requestTicketsDto.getDepartureCity()))))
                    .andExpect(jsonPath("$[*].arrivalCity").value(everyItem(is(requestTicketsDto.getArrivalCity()))))
                    .andExpect(jsonPath("$[*].departureTime").exists())
                    .andExpect(jsonPath("$[*].departureDate").value(everyItem(is(departureDate))))
                    .andExpect(jsonPath("$[*].arrivalTime").exists())
                    .andExpect(jsonPath("$[*].arrivalDate").exists())
                    .andExpect(jsonPath("$[*].travelTime").exists())
                    .andExpect(jsonPath("$[*].carrier").exists())
                    .andReturn();

            assertTrue(checkSort(language, criteria, sortType, mvcResult));
        }


        @Test
        @SneakyThrows
        @DisplayName("[200] sort by price in asc order with ENG language")
        void getSortedTicketsByPriceAscEngIs200() {
            ContentLanguage language = ContentLanguage.ENG;
            SortCriteria criteria = SortCriteria.PRICE;
            SortType sortType = SortType.ASC;

            RequestSortedTicketsDto requestBody = requestTicketsDto;
            requestBody.setSortingBy(Map.of(criteria, sortType));

            String departureDate = CustomDateTimeFormatter.formatDate(requestBody.getDepartureDate(), language.getLanguage());

            MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(URL + "/sortBy")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(requestTicketsDto))
                            .header(HttpHeaders.CONTENT_LANGUAGE, language).characterEncoding(StandardCharsets.UTF_8)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(NUMBER_OF_TICKETS)))
                    .andExpect(jsonPath("$[*].id").exists())
                    .andExpect(jsonPath("$[*].type").exists())
                    .andExpect(jsonPath("$[*].placeFrom").exists())
                    .andExpect(jsonPath("$[*].placeAt").exists())
                    .andExpect(jsonPath("$[*].departureCity").value(everyItem(is(requestBody.getDepartureCity()))))
                    .andExpect(jsonPath("$[*].arrivalCity").value(everyItem(is(requestBody.getArrivalCity()))))
                    .andExpect(jsonPath("$[*].departureTime").exists())
                    .andExpect(jsonPath("$[*].departureDate").value(everyItem(is(departureDate))))
                    .andExpect(jsonPath("$[*].arrivalTime").exists())
                    .andExpect(jsonPath("$[*].arrivalDate").exists())
                    .andExpect(jsonPath("$[*].travelTime").exists())
                    .andExpect(jsonPath("$[*].carrier").exists())
                    .andReturn();

            assertTrue(checkSort(language, criteria, sortType, mvcResult));
        }

        @Test
        @SneakyThrows
        @DisplayName("[200] sort by price in desc order with UA language")
        void getSortedTicketsByPriceDescUaIs200() {
            ContentLanguage language = ContentLanguage.UA;
            SortCriteria criteria = SortCriteria.PRICE;
            SortType sortType = SortType.DESC;

            RequestSortedTicketsDto requestBody = requestTicketsDto;
            requestBody.setSortingBy(Map.of(criteria, sortType));

            String departureDate = CustomDateTimeFormatter.formatDate(requestBody.getDepartureDate(), language.getLanguage());

            MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(URL + "/sortBy")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(requestTicketsDto))
                            .header(HttpHeaders.CONTENT_LANGUAGE, language).characterEncoding(StandardCharsets.UTF_8)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(NUMBER_OF_TICKETS)))
                    .andExpect(jsonPath("$[*].id").exists())
                    .andExpect(jsonPath("$[*].type").exists())
                    .andExpect(jsonPath("$[*].placeFrom").exists())
                    .andExpect(jsonPath("$[*].placeAt").exists())
                    .andExpect(jsonPath("$[*].departureCity").value(everyItem(is(requestBody.getDepartureCity()))))
                    .andExpect(jsonPath("$[*].arrivalCity").value(everyItem(is(requestBody.getArrivalCity()))))
                    .andExpect(jsonPath("$[*].departureTime").exists())
                    .andExpect(jsonPath("$[*].departureDate").value(everyItem(is(departureDate))))
                    .andExpect(jsonPath("$[*].arrivalTime").exists())
                    .andExpect(jsonPath("$[*].arrivalDate").exists())
                    .andExpect(jsonPath("$[*].travelTime").exists())
                    .andExpect(jsonPath("$[*].carrier").exists())
                    .andReturn();

            assertTrue(checkSort(language, criteria, sortType, mvcResult));
        }

        @Test
        @SneakyThrows
        @DisplayName("[200] sort by price in desc order with ENG language")
        void getSortedTicketsByPriceDescEngIs200() {
            ContentLanguage language = ContentLanguage.ENG;
            SortCriteria criteria = SortCriteria.PRICE;
            SortType sortType = SortType.DESC;

            RequestSortedTicketsDto requestBody = requestTicketsDto;
            requestBody.setSortingBy(Map.of(criteria, sortType));

            String departureDate = CustomDateTimeFormatter.formatDate(requestBody.getDepartureDate(), language.getLanguage());

            MvcResult result = mvc.perform(MockMvcRequestBuilders.post(URL + "/sortBy")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(requestTicketsDto))
                            .header(HttpHeaders.CONTENT_LANGUAGE, language).characterEncoding(StandardCharsets.UTF_8)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(NUMBER_OF_TICKETS)))
                    .andExpect(jsonPath("$[*].id").exists())
                    .andExpect(jsonPath("$[*].type").exists())
                    .andExpect(jsonPath("$[*].placeFrom").exists())
                    .andExpect(jsonPath("$[*].placeAt").exists())
                    .andExpect(jsonPath("$[*].departureCity").value(everyItem(is(requestBody.getDepartureCity()))))
                    .andExpect(jsonPath("$[*].arrivalCity").value(everyItem(is(requestBody.getArrivalCity()))))
                    .andExpect(jsonPath("$[*].departureTime").exists())
                    .andExpect(jsonPath("$[*].departureDate").value(everyItem(is(departureDate))))
                    .andExpect(jsonPath("$[*].arrivalTime").exists())
                    .andExpect(jsonPath("$[*].arrivalDate").exists())
                    .andExpect(jsonPath("$[*].travelTime").exists())
                    .andExpect(jsonPath("$[*].carrier").exists())
                    .andReturn();

            assertTrue(checkSort(language, criteria, sortType, result));
        }

        @Test
        @SneakyThrows
        @DisplayName("[200] sort by arrival time in asc order with UA language")
        void getSortedTicketsByArrivalTimeAscUaIs200() {
            ContentLanguage language = ContentLanguage.UA;
            SortCriteria criteria = SortCriteria.ARRIVAL_TIME;
            SortType sortType = SortType.ASC;

            RequestSortedTicketsDto requestBody = requestTicketsDto;
            requestBody.setSortingBy(Map.of(criteria, sortType));

            String departureDate = CustomDateTimeFormatter.formatDate(requestBody.getDepartureDate(), language.getLanguage());

            MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(URL + "/sortBy")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(requestTicketsDto))
                            .header(HttpHeaders.CONTENT_LANGUAGE, language).characterEncoding(StandardCharsets.UTF_8)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(NUMBER_OF_TICKETS)))
                    .andExpect(jsonPath("$[*].id").exists())
                    .andExpect(jsonPath("$[*].type").exists())
                    .andExpect(jsonPath("$[*].placeFrom").exists())
                    .andExpect(jsonPath("$[*].placeAt").exists())
                    .andExpect(jsonPath("$[*].departureCity").value(everyItem(is(requestBody.getDepartureCity()))))
                    .andExpect(jsonPath("$[*].arrivalCity").value(everyItem(is(requestBody.getArrivalCity()))))
                    .andExpect(jsonPath("$[*].departureTime").exists())
                    .andExpect(jsonPath("$[*].departureDate").value(everyItem(is(departureDate))))
                    .andExpect(jsonPath("$[*].arrivalTime").exists())
                    .andExpect(jsonPath("$[*].arrivalDate").exists())
                    .andExpect(jsonPath("$[*].travelTime").exists())
                    .andExpect(jsonPath("$[*].price").exists())
                    .andExpect(jsonPath("$[*].carrier").exists())
                    .andReturn();

            assertTrue(checkSort(language, criteria, sortType, mvcResult));
        }

        @Test
        @SneakyThrows
        @DisplayName("[200] sort by arrival time in asc order with ENG language")
        void getSortedTicketsByArrivalTimeAscEngIs200() {
            ContentLanguage language = ContentLanguage.ENG;
            SortCriteria criteria = SortCriteria.ARRIVAL_TIME;
            SortType sortType = SortType.ASC;

            RequestSortedTicketsDto requestBody = requestTicketsDto;
            requestBody.setSortingBy(Map.of(criteria, sortType));

            String departureDate = CustomDateTimeFormatter.formatDate(requestBody.getDepartureDate(), language.getLanguage());

            MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(URL + "/sortBy")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(requestTicketsDto))
                            .header(HttpHeaders.CONTENT_LANGUAGE, language).characterEncoding(StandardCharsets.UTF_8)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(NUMBER_OF_TICKETS)))
                    .andExpect(jsonPath("$[*].id").exists())
                    .andExpect(jsonPath("$[*].type").exists())
                    .andExpect(jsonPath("$[*].placeFrom").exists())
                    .andExpect(jsonPath("$[*].placeAt").exists())
                    .andExpect(jsonPath("$[*].departureCity").value(everyItem(is(requestTicketsDto.getDepartureCity()))))
                    .andExpect(jsonPath("$[*].arrivalCity").value(everyItem(is(requestTicketsDto.getArrivalCity()))))
                    .andExpect(jsonPath("$[*].departureTime").exists())
                    .andExpect(jsonPath("$[*].departureDate").value(everyItem(is(departureDate))))
                    .andExpect(jsonPath("$[*].arrivalTime").exists())
                    .andExpect(jsonPath("$[*].arrivalDate").exists())
                    .andExpect(jsonPath("$[*].travelTime").exists())
                    .andExpect(jsonPath("$[*].price").exists())
                    .andExpect(jsonPath("$[*].carrier").exists())
                    .andReturn();

            assertTrue(checkSort(language, criteria, sortType, mvcResult));
        }

        @Test
        @SneakyThrows
        @DisplayName("[200] sort by arrival time in desc order with UA language")
        void getSortedTicketsByArrivalTimeDescUaIs200() {
            ContentLanguage language = ContentLanguage.UA;
            SortCriteria criteria = SortCriteria.ARRIVAL_TIME;
            SortType sortType = SortType.DESC;

            RequestSortedTicketsDto requestBody = requestTicketsDto;
            requestBody.setSortingBy(Map.of(criteria, sortType));

            String departureDate = CustomDateTimeFormatter.formatDate(requestBody.getDepartureDate(), language.getLanguage());

            MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(URL + "/sortBy")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(requestTicketsDto))
                            .header(HttpHeaders.CONTENT_LANGUAGE, language).characterEncoding(StandardCharsets.UTF_8)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[99].arrivalTime").value(lessThan("$[0].arrivalTime")))
                    .andExpect(jsonPath("$", hasSize(NUMBER_OF_TICKETS)))
                    .andExpect(jsonPath("$[*].id").exists())
                    .andExpect(jsonPath("$[*].type").exists())
                    .andExpect(jsonPath("$[*].placeFrom").exists())
                    .andExpect(jsonPath("$[*].placeAt").exists())
                    .andExpect(jsonPath("$[*].departureCity").value(everyItem(is(requestTicketsDto.getDepartureCity()))))
                    .andExpect(jsonPath("$[*].arrivalCity").value(everyItem(is(requestTicketsDto.getArrivalCity()))))
                    .andExpect(jsonPath("$[*].departureTime").exists())
                    .andExpect(jsonPath("$[*].departureDate").value(everyItem(is(departureDate))))
                    .andExpect(jsonPath("$[*].arrivalTime").exists())
                    .andExpect(jsonPath("$[*].arrivalDate").exists())
                    .andExpect(jsonPath("$[*].travelTime").exists())
                    .andExpect(jsonPath("$[*].price").exists())
                    .andExpect(jsonPath("$[*].carrier").exists())
                    .andReturn();

            assertTrue(checkSort(language, criteria, sortType, mvcResult));
        }

        @Test
        @SneakyThrows
        @DisplayName("[200] sort by arrival time in desc order with ENG language")
        void getSortedTicketsByArrivalTimeDescEngIs200() {
            ContentLanguage language = ContentLanguage.ENG;
            SortCriteria criteria = SortCriteria.ARRIVAL_TIME;
            SortType sortType = SortType.DESC;

            RequestSortedTicketsDto requestBody = requestTicketsDto;
            requestBody.setSortingBy(Map.of(criteria, sortType));

            String departureDate = CustomDateTimeFormatter.formatDate(requestBody.getDepartureDate(), language.getLanguage());
            MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(URL + "/sortBy")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(requestTicketsDto))
                            .header(HttpHeaders.CONTENT_LANGUAGE, language).characterEncoding(StandardCharsets.UTF_8)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(NUMBER_OF_TICKETS)))
                    .andExpect(jsonPath("$[*].id").exists())
                    .andExpect(jsonPath("$[*].type").exists())
                    .andExpect(jsonPath("$[*].placeFrom").exists())
                    .andExpect(jsonPath("$[*].placeAt").exists())
                    .andExpect(jsonPath("$[*].departureCity").value(everyItem(is(requestTicketsDto.getDepartureCity()))))
                    .andExpect(jsonPath("$[*].arrivalCity").value(everyItem(is(requestTicketsDto.getArrivalCity()))))
                    .andExpect(jsonPath("$[*].departureTime").exists())
                    .andExpect(jsonPath("$[*].departureDate").value(everyItem(is(departureDate))))
                    .andExpect(jsonPath("$[*].arrivalTime").exists())
                    .andExpect(jsonPath("$[*].arrivalDate").exists())
                    .andExpect(jsonPath("$[*].travelTime").exists())
                    .andExpect(jsonPath("$[*].price").exists())
                    .andExpect(jsonPath("$[*].carrier").exists())
                    .andReturn();

            assertTrue(checkSort(language, criteria, sortType, mvcResult));
        }

        @Test
        @SneakyThrows
        @DisplayName("[200] sort by departure time in asc order with UA language")
        void getSortedTicketsByDepartureTimeAscUaIs200() {
            ContentLanguage language = ContentLanguage.UA;
            SortCriteria criteria = SortCriteria.DEPARTURE_TIME;
            SortType sortType = SortType.ASC;

            RequestSortedTicketsDto requestBody = requestTicketsDto;
            requestBody.setSortingBy(Map.of(criteria, sortType));

            String departureDate = CustomDateTimeFormatter.formatDate(requestBody.getDepartureDate(), language.getLanguage());

            MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(URL + "/sortBy")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(requestTicketsDto))
                            .header(HttpHeaders.CONTENT_LANGUAGE, language).characterEncoding(StandardCharsets.UTF_8)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(NUMBER_OF_TICKETS)))
                    .andExpect(jsonPath("$[*].id").exists())
                    .andExpect(jsonPath("$[*].type").exists())
                    .andExpect(jsonPath("$[*].placeFrom").exists())
                    .andExpect(jsonPath("$[*].placeAt").exists())
                    .andExpect(jsonPath("$[*].departureCity").value(everyItem(is(requestTicketsDto.getDepartureCity()))))
                    .andExpect(jsonPath("$[*].arrivalCity").value(everyItem(is(requestTicketsDto.getArrivalCity()))))
                    .andExpect(jsonPath("$[*].departureDate").value(everyItem(is(departureDate))))
                    .andExpect(jsonPath("$[*].arrivalTime").exists())
                    .andExpect(jsonPath("$[*].arrivalDate").exists())
                    .andExpect(jsonPath("$[*].travelTime").exists())
                    .andExpect(jsonPath("$[*].price").exists())
                    .andExpect(jsonPath("$[*].carrier").exists())
                    .andReturn();

            assertTrue(checkSort(language, criteria, sortType, mvcResult));
        }

        @Test
        @SneakyThrows
        @DisplayName("[200] sort by departure time in asc order with ENG language")
        void getSortedTicketsByDepartureTimeAscEngIs200() {
            ContentLanguage language = ContentLanguage.ENG;
            SortCriteria criteria = SortCriteria.DEPARTURE_TIME;
            SortType sortType = SortType.ASC;

            RequestSortedTicketsDto requestBody = requestTicketsDto;
            requestBody.setSortingBy(Map.of(criteria, sortType));

            String departureDate = CustomDateTimeFormatter.formatDate(requestBody.getDepartureDate(), language.getLanguage());

            MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(URL + "/sortBy")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(requestTicketsDto))
                            .header(HttpHeaders.CONTENT_LANGUAGE, language).characterEncoding(StandardCharsets.UTF_8)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(NUMBER_OF_TICKETS)))
                    .andExpect(jsonPath("$[*].id").exists())
                    .andExpect(jsonPath("$[*].type").exists())
                    .andExpect(jsonPath("$[*].placeFrom").exists())
                    .andExpect(jsonPath("$[*].placeAt").exists())
                    .andExpect(jsonPath("$[*].departureCity").value(everyItem(is(requestTicketsDto.getDepartureCity()))))
                    .andExpect(jsonPath("$[*].arrivalCity").value(everyItem(is(requestTicketsDto.getArrivalCity()))))
                    .andExpect(jsonPath("$[*].departureDate").value(everyItem(is(departureDate))))
                    .andExpect(jsonPath("$[*].arrivalTime").exists())
                    .andExpect(jsonPath("$[*].arrivalDate").exists())
                    .andExpect(jsonPath("$[*].travelTime").exists())
                    .andExpect(jsonPath("$[*].price").exists())
                    .andExpect(jsonPath("$[*].carrier").exists())
                    .andReturn();

            assertTrue(checkSort(language, criteria, sortType, mvcResult));
        }

        @Test
        @SneakyThrows
        @DisplayName("[200] sort by departure time in desc order with UA language")
        void getSortedTicketsByDepartureTimeDescUaIs200() {
            ContentLanguage language = ContentLanguage.UA;
            SortCriteria criteria = SortCriteria.DEPARTURE_TIME;
            SortType sortType = SortType.DESC;

            RequestSortedTicketsDto requestBody = requestTicketsDto;
            requestBody.setSortingBy(Map.of(criteria, sortType));

            String departureDate = CustomDateTimeFormatter.formatDate(requestBody.getDepartureDate(), language.getLanguage());
            MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(URL + "/sortBy")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(requestTicketsDto))
                            .header(HttpHeaders.CONTENT_LANGUAGE, language).characterEncoding(StandardCharsets.UTF_8)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(NUMBER_OF_TICKETS)))
                    .andExpect(jsonPath("$[*].id").exists())
                    .andExpect(jsonPath("$[*].type").exists())
                    .andExpect(jsonPath("$[*].placeFrom").exists())
                    .andExpect(jsonPath("$[*].placeAt").exists())
                    .andExpect(jsonPath("$[*].departureCity").value(everyItem(is(requestTicketsDto.getDepartureCity()))))
                    .andExpect(jsonPath("$[*].arrivalCity").value(everyItem(is(requestTicketsDto.getArrivalCity()))))
                    .andExpect(jsonPath("$[*].departureDate").value(everyItem(is(departureDate))))
                    .andExpect(jsonPath("$[*].arrivalTime").exists())
                    .andExpect(jsonPath("$[*].arrivalDate").exists())
                    .andExpect(jsonPath("$[*].travelTime").exists())
                    .andExpect(jsonPath("$[*].price").exists())
                    .andExpect(jsonPath("$[*].carrier").exists())
                    .andReturn();

            assertTrue(checkSort(language, criteria, sortType, mvcResult));
        }

        @Test
        @SneakyThrows
        @DisplayName("[200] sort by departure time in desc order with ENG language")
        void getSortedTicketsByDepartureTimeDescEngIs200() {
            ContentLanguage language = ContentLanguage.ENG;
            SortCriteria criteria = SortCriteria.DEPARTURE_TIME;
            SortType sortType = SortType.DESC;

            RequestSortedTicketsDto requestBody = requestTicketsDto;
            requestBody.setSortingBy(Map.of(criteria, sortType));

            String departureDate = CustomDateTimeFormatter.formatDate(requestBody.getDepartureDate(), language.getLanguage());

            MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(URL + "/sortBy")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(requestTicketsDto))
                            .header(HttpHeaders.CONTENT_LANGUAGE, language).characterEncoding(StandardCharsets.UTF_8)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(NUMBER_OF_TICKETS)))
                    .andExpect(jsonPath("$[*].id").exists())
                    .andExpect(jsonPath("$[*].type").exists())
                    .andExpect(jsonPath("$[*].placeFrom").exists())
                    .andExpect(jsonPath("$[*].placeAt").exists())
                    .andExpect(jsonPath("$[*].departureCity").value(everyItem(is(requestTicketsDto.getDepartureCity()))))
                    .andExpect(jsonPath("$[*].arrivalCity").value(everyItem(is(requestTicketsDto.getArrivalCity()))))
                    .andExpect(jsonPath("$[*].departureDate").value(everyItem(is(departureDate))))

                    .andExpect(jsonPath("$[*].arrivalTime").exists()).andExpect(jsonPath("$[*].arrivalDate").exists())
                    .andExpect(jsonPath("$[*].travelTime").exists())
                    .andExpect(jsonPath("$[*].price").exists())
                    .andExpect(jsonPath("$[*].carrier").exists())
                    .andReturn();

            assertTrue(checkSort(language, criteria, sortType, mvcResult));
        }

        @Test
        @SneakyThrows
        @DisplayName("[200] sort by travel time in asc order with UA language")
        void getSortedTicketsByTravelTimeAscUaIs200() {
            ContentLanguage language = ContentLanguage.UA;
            SortCriteria criteria = SortCriteria.TRAVEL_TIME;
            SortType sortType = SortType.ASC;

            RequestSortedTicketsDto requestBody = requestTicketsDto;
            requestBody.setSortingBy(Map.of(criteria, sortType));

            String departureDate = CustomDateTimeFormatter.formatDate(requestBody.getDepartureDate(), language.getLanguage());
            MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(URL + "/sortBy")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(requestTicketsDto))
                            .header(HttpHeaders.CONTENT_LANGUAGE, language).characterEncoding(StandardCharsets.UTF_8)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(NUMBER_OF_TICKETS)))
                    .andExpect(jsonPath("$[*].id").exists())
                    .andExpect(jsonPath("$[*].type").exists())
                    .andExpect(jsonPath("$[*].placeFrom").exists())
                    .andExpect(jsonPath("$[*].placeAt").exists())
                    .andExpect(jsonPath("$[*].departureCity").value(everyItem(is(requestTicketsDto.getDepartureCity()))))
                    .andExpect(jsonPath("$[*].arrivalCity").value(everyItem(is(requestTicketsDto.getArrivalCity()))))
                    .andExpect(jsonPath("$[*].departureDate").value(everyItem(is(departureDate))))
                    .andExpect(jsonPath("$[*].departureTime").exists())
                    .andExpect(jsonPath("$[*].arrivalTime").exists())
                    .andExpect(jsonPath("$[*].arrivalDate").exists())
                    .andExpect(jsonPath("$[*].price").exists())
                    .andExpect(jsonPath("$[*].carrier").exists())
                    .andReturn();

            assertTrue(checkSort(language, criteria, sortType, mvcResult));
        }

        @Test
        @SneakyThrows
        @DisplayName("[200] sort by travel time in asc order with ENG language")
        void getSortedTicketsByTravelTimeAscEngIs200() {
            ContentLanguage language = ContentLanguage.ENG;
            SortCriteria criteria = SortCriteria.TRAVEL_TIME;
            SortType sortType = SortType.ASC;

            RequestSortedTicketsDto requestBody = requestTicketsDto;
            requestBody.setSortingBy(Map.of(criteria, sortType));

            String departureDate = CustomDateTimeFormatter.formatDate(requestBody.getDepartureDate(), language.getLanguage());

            MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(URL + "/sortBy")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(requestTicketsDto))
                            .header(HttpHeaders.CONTENT_LANGUAGE, language).characterEncoding(StandardCharsets.UTF_8)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(NUMBER_OF_TICKETS)))
                    .andExpect(jsonPath("$[*].id").exists())
                    .andExpect(jsonPath("$[*].type").exists())
                    .andExpect(jsonPath("$[*].placeFrom").exists())
                    .andExpect(jsonPath("$[*].placeAt").exists())
                    .andExpect(jsonPath("$[*].departureCity").value(everyItem(is(requestTicketsDto.getDepartureCity()))))
                    .andExpect(jsonPath("$[*].arrivalCity").value(everyItem(is(requestTicketsDto.getArrivalCity()))))
                    .andExpect(jsonPath("$[*].departureDate").value(everyItem(is(departureDate))))
                    .andExpect(jsonPath("$[*].departureTime").exists())
                    .andExpect(jsonPath("$[*].arrivalTime").exists())
                    .andExpect(jsonPath("$[*].arrivalDate").exists())
                    .andExpect(jsonPath("$[*].price").exists())
                    .andExpect(jsonPath("$[*].carrier").exists())
                    .andReturn();

            assertTrue(checkSort(language, criteria, sortType, mvcResult));
        }

        @Test
        @SneakyThrows
        @DisplayName("[200] sort by travel time in desc order with UA language")
        void getSortedTicketsByTravelTimeDescUaIs200() {
            ContentLanguage language = ContentLanguage.UA;
            SortCriteria criteria = SortCriteria.TRAVEL_TIME;
            SortType sortType = SortType.DESC;

            RequestSortedTicketsDto requestBody = requestTicketsDto;
            requestBody.setSortingBy(Map.of(criteria, sortType));


            String departureDate = CustomDateTimeFormatter.formatDate(requestBody.getDepartureDate(), language.getLanguage());
            MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(URL + "/sortBy")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(requestTicketsDto))
                            .header(HttpHeaders.CONTENT_LANGUAGE, language).characterEncoding(StandardCharsets.UTF_8)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(NUMBER_OF_TICKETS)))
                    .andExpect(jsonPath("$[*].id").exists())
                    .andExpect(jsonPath("$[*].type").exists())
                    .andExpect(jsonPath("$[*].placeFrom").exists())
                    .andExpect(jsonPath("$[*].placeAt").exists())
                    .andExpect(jsonPath("$[*].departureCity").value(everyItem(is(requestTicketsDto.getDepartureCity()))))
                    .andExpect(jsonPath("$[*].arrivalCity").value(everyItem(is(requestTicketsDto.getArrivalCity()))))
                    .andExpect(jsonPath("$[*].departureDate").value(everyItem(is(departureDate))))
                    .andExpect(jsonPath("$[*].departureTime").exists())
                    .andExpect(jsonPath("$[*].arrivalTime").exists())
                    .andExpect(jsonPath("$[*].arrivalDate").exists())
                    .andExpect(jsonPath("$[*].price").exists())
                    .andExpect(jsonPath("$[*].carrier").exists())
                    .andReturn();

            assertTrue(checkSort(language, criteria, sortType, mvcResult));
        }

        @Test
        @SneakyThrows
        @DisplayName("[200] sort by travel time in desc order with ENG language")
        void getSortedTicketsByTravelTimeDescEngIs200() {
            ContentLanguage language = ContentLanguage.ENG;
            SortCriteria criteria = SortCriteria.TRAVEL_TIME;
            SortType sortType = SortType.DESC;

            RequestSortedTicketsDto requestBody = requestTicketsDto;
            requestBody.setSortingBy(Map.of(criteria, sortType));

            String departureDate = CustomDateTimeFormatter.formatDate(requestBody.getDepartureDate(), language.getLanguage());

            MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(URL + "/sortBy")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(requestTicketsDto))
                            .header(HttpHeaders.CONTENT_LANGUAGE, language).characterEncoding(StandardCharsets.UTF_8)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(NUMBER_OF_TICKETS)))
                    .andExpect(jsonPath("$[*].id").exists())
                    .andExpect(jsonPath("$[*].type").exists())
                    .andExpect(jsonPath("$[*].placeFrom").exists())
                    .andExpect(jsonPath("$[*].placeAt").exists())
                    .andExpect(jsonPath("$[*].departureCity").value(everyItem(is(requestTicketsDto.getDepartureCity()))))
                    .andExpect(jsonPath("$[*].arrivalCity").value(everyItem(is(requestTicketsDto.getArrivalCity()))))
                    .andExpect(jsonPath("$[*].departureDate").value(everyItem(is(departureDate))))
                    .andExpect(jsonPath("$[*].departureTime").exists())
                    .andExpect(jsonPath("$[*].arrivalTime").exists())
                    .andExpect(jsonPath("$[*].arrivalDate").exists())
                    .andExpect(jsonPath("$[*].price").exists())
                    .andExpect(jsonPath("$[*].carrier").exists())
                    .andReturn();

            assertTrue(checkSort(language, criteria, sortType, mvcResult));
        }
    }

}
