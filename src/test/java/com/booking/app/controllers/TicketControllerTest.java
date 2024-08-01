package com.booking.app.controllers;


import com.booking.app.constants.ContentLanguage;
import com.booking.app.constants.SiteConstants;
import com.booking.app.constants.SortCriteria;
import com.booking.app.constants.SortType;
import com.booking.app.dto.tickets.RequestSortedTicketsDto;
import com.booking.app.entities.ticket.Route;
import com.booking.app.entities.ticket.bus.BusInfo;
import com.booking.app.entities.ticket.bus.BusTicket;
import com.booking.app.entities.ticket.train.TrainTicket;
import com.booking.app.mappers.TicketMapper;
import com.booking.app.repositories.RouteRepository;
import com.booking.app.repositories.TicketRepository;
import com.fasterxml.jackson.databind.JsonNode;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

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
    private static final int NUMBER_OF_TICKETS = 100;


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

    private static List<BusInfo> getGenerateBusInfo(BusTicket busTicket) {
        int numberOfInfoBuses = new Random().nextInt(1, 4);
        List<BusInfo> busInfos = new ArrayList<>();
        for (int i = 0; i < numberOfInfoBuses; i++) {
            busInfos.add(BusInfo.builder()
                    .price(new BigDecimal(new Random().nextInt(100, 5000)))
                    .link(getRandomSite())
                    .sourceWebsite(RandomStringUtils.randomAlphabetic(20))
                    .busTicket(busTicket)
                    .build());
        }
        return busInfos;

    }

    private static String getRandomSite() {
        List<String> sites = List.of(
                SiteConstants.PROIZD_UA,
                SiteConstants.BUSFOR_UA,
                SiteConstants.INFOBUS
        );

        int randomIndex = new Random().nextInt(sites.size());
        return sites.get(randomIndex);
    }

    private static LocalTime between(LocalTime startTime, LocalTime endTime) {
        int startSeconds = startTime.toSecondOfDay();
        int endSeconds = endTime.toSecondOfDay();
        int randomTime = ThreadLocalRandom
                .current()
                .nextInt(startSeconds, endSeconds);

        return LocalTime.ofSecondOfDay(randomTime);
    }

    public static Instant getRandomInstantWithinThreeDays() {
        Instant now = Instant.now();
        Instant threeDaysFromNow = now.plus(Duration.ofDays(3));

        long randomEpochSecond = ThreadLocalRandom.current().nextLong(now.getEpochSecond(), threeDaysFromNow.getEpochSecond());
        return Instant.ofEpochSecond(randomEpochSecond);
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

        for (int i = 0; i < NUMBER_OF_TICKETS; i++) {
            BusTicket busTicket = BusTicket.builder()
                    .id(UUID.randomUUID())
                    .route(route)
                    .placeAt(RandomStringUtils.randomAlphabetic(20))
                    .placeFrom(RandomStringUtils.randomAlphabetic(20))
                    .departureTime(between(LocalTime.MIN, LocalTime.MAX.minus(Duration.ofHours(10))))
                    .arrivalDateTime(getRandomInstantWithinThreeDays())
                    .travelTime(RandomUtils.nextInt(10000))
                    .carrier(RandomStringUtils.randomAlphabetic(10))
                    .build();
            busTickets.add(busTicket);
            List<BusInfo> busInfos = getGenerateBusInfo(busTicket);
            busTicket.setInfoList(busInfos);
        }
//        for (int i = 0; i < 50; i++) {
//            trainTickets.add(TrainTicket.builder()
//                    .id(UUID.randomUUID())
//                    .route(route)
//                    .placeAt(RandomStringUtils.randomAlphabetic(20))
//                    .placeFrom(RandomStringUtils.randomAlphabetic(20))
//                    .arrivalTimeTime(Instant.now())
//                    .travelTime(RandomUtils.nextInt(10000))
//                    .carrier(RandomStringUtils.randomAlphabetic(10))
//                    .departureTime(between(LocalTime.MIN, LocalTime.MAX))
//                    .build());
//        }
        routeRepository.save(route);
        ticketRepository.saveAll(busTickets);
        ticketRepository.saveAll(trainTickets);
    }

    @SneakyThrows
    private boolean checkSort(SortCriteria criteria, SortType sortType, MvcResult result) {
        String contentAsString = result.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(contentAsString);

        if (criteria.getCriteria() == SortCriteria.ARRIVAL_TIME.getCriteria()) {
            LocalTime timeFirst = LocalTime.parse(jsonNode.get(0).get(criteria.getCriteria()).asText());
            LocalTime timeLast = LocalTime.parse(jsonNode.get(NUMBER_OF_TICKETS - 1).get(criteria.getCriteria()).asText());
            return timeFirst.isBefore(timeLast);
        }

        double first = jsonNode.get(0).get(criteria.getCriteria()).asDouble();
        double last = jsonNode.get(NUMBER_OF_TICKETS - 1).get(criteria.getCriteria()).asDouble();
        if (sortType.equals(SortType.ASC)) {
            return first < last;
        } else {
            return first > last;
        }
    }

    @Nested
    @DisplayName("POST get sorted tickets")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class Post {
        private RequestSortedTicketsDto requestTicketsDto;

        private Map<SortCriteria, SortType> getCriteriaMap(SortCriteria sortCriteria, SortType sortType) {
            Map<SortCriteria, SortType> sortCriteriaSortTypeMap = new HashMap<>();
            sortCriteriaSortTypeMap.put(sortCriteria, sortType);
            return sortCriteriaSortTypeMap;
        }


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
            RequestSortedTicketsDto requestBody = requestTicketsDto;
            mvc.perform(MockMvcRequestBuilders.post(URL + "/sortBy")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(requestTicketsDto))
                            .header(HttpHeaders.CONTENT_LANGUAGE, UA)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(100)))
                    .andExpect(jsonPath("$[*].departureCity").value(everyItem(is(requestBody.getDepartureCity()))))
                    .andExpect(jsonPath("$[*].arrivalCity").value(everyItem(is(requestBody.getArrivalCity()))))
                    .andExpect(jsonPath("$[*].departureDate").value(everyItem(is(TicketMapper.departureTimeMapping(requestBody.getDepartureDate().toString(), UA)))))
                    .andExpect(jsonPath("$[*].id").exists())
                    .andExpect(jsonPath("$[*].type").exists())
                    .andExpect(jsonPath("$[*].placeFrom").exists())
                    .andExpect(jsonPath("$[*].placeAt").exists())
                    .andExpect(jsonPath("$[*].departureTime").exists())

                    .andExpect(jsonPath("$[*].arrivalTime").exists()).andExpect(jsonPath("$[*].arrivalDate").exists())
                    .andExpect(jsonPath("$[*].travelTime").exists())
                    .andExpect(jsonPath("$[*].price").exists())
                    .andExpect(jsonPath("$[*].carrier").exists());
        }

        @Test
        @SneakyThrows
        @DisplayName("[200] sort without any criteria with ENG language")
        void getSortedTicketsEngIs200() {
            RequestSortedTicketsDto requestBody = requestTicketsDto;
            mvc.perform(MockMvcRequestBuilders.post(URL + "/sortBy")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(requestTicketsDto))
                            .header(HttpHeaders.CONTENT_LANGUAGE, ENG)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(100)))
                    .andExpect(jsonPath("$[*].departureCity").value(everyItem(is(requestBody.getDepartureCity()))))
                    .andExpect(jsonPath("$[*].arrivalCity").value(everyItem(is(requestBody.getArrivalCity()))))
                    .andExpect(jsonPath("$[*].departureDate").value(everyItem(is(TicketMapper.departureTimeMapping(requestBody.getDepartureDate().toString(), ENG)))))
                    .andExpect(jsonPath("$[*].id").exists())
                    .andExpect(jsonPath("$[*].type").exists())
                    .andExpect(jsonPath("$[*].placeFrom").exists())
                    .andExpect(jsonPath("$[*].placeAt").exists())
                    .andExpect(jsonPath("$[*].departureTime").exists())

                    .andExpect(jsonPath("$[*].arrivalTime").exists()).andExpect(jsonPath("$[*].arrivalDate").exists())
                    .andExpect(jsonPath("$[*].travelTime").exists())
                    .andExpect(jsonPath("$[*].price").exists())
                    .andExpect(jsonPath("$[*].carrier").exists());
        }

        @Test
        @SneakyThrows
        @DisplayName("[200] sort by price in asc order with UA language")
        void getSortedTicketsByPriceAscUaIs200() {
            RequestSortedTicketsDto dto = requestTicketsDto;
            dto.setSortingBy(getCriteriaMap(SortCriteria.PRICE, SortType.ASC));
            MvcResult result = mvc.perform(MockMvcRequestBuilders.post(URL + "/sortBy")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(requestTicketsDto))
                            .header(HttpHeaders.CONTENT_LANGUAGE, UA)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(100)))
                    .andExpect(jsonPath("$[*].id").exists())
                    .andExpect(jsonPath("$[*].type").exists())
                    .andExpect(jsonPath("$[*].placeFrom").exists())
                    .andExpect(jsonPath("$[*].placeAt").exists())
                    .andExpect(jsonPath("$[*].departureCity").value(everyItem(is(requestTicketsDto.getDepartureCity()))))
                    .andExpect(jsonPath("$[*].arrivalCity").value(everyItem(is(requestTicketsDto.getArrivalCity()))))
                    .andExpect(jsonPath("$[*].departureTime").exists())
                    .andExpect(jsonPath("$[*].departureDate").value(everyItem(is(TicketMapper.departureTimeMapping(requestTicketsDto.getDepartureDate().toString(), UA)))))

                    .andExpect(jsonPath("$[*].arrivalTime").exists()).andExpect(jsonPath("$[*].arrivalDate").exists())
                    .andExpect(jsonPath("$[*].travelTime").exists())
                    .andExpect(jsonPath("$[*].carrier").exists())
                    .andReturn();


            Assertions.assertTrue(checkSort(SortCriteria.PRICE, SortType.ASC, result));

        }


        @Test
        @SneakyThrows
        @DisplayName("[200] sort by price in asc order with ENG language")
        void getSortedTicketsByPriceAscEngIs200() {
            RequestSortedTicketsDto dto = requestTicketsDto;
            dto.setSortingBy(getCriteriaMap(SortCriteria.PRICE, SortType.ASC));
            MvcResult result = mvc.perform(MockMvcRequestBuilders.post(URL + "/sortBy")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(requestTicketsDto))
                            .header(HttpHeaders.CONTENT_LANGUAGE, ENG)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(100)))
                    .andExpect(jsonPath("$[*].id").exists())
                    .andExpect(jsonPath("$[*].type").exists())
                    .andExpect(jsonPath("$[*].placeFrom").exists())
                    .andExpect(jsonPath("$[*].placeAt").exists())
                    .andExpect(jsonPath("$[*].departureCity").value(everyItem(is(dto.getDepartureCity()))))
                    .andExpect(jsonPath("$[*].arrivalCity").value(everyItem(is(dto.getArrivalCity()))))
                    .andExpect(jsonPath("$[*].departureTime").exists())
                    .andExpect(jsonPath("$[*].departureDate").value(everyItem(is(TicketMapper.departureTimeMapping(dto.getDepartureDate().toString(), ENG)))))

                    .andExpect(jsonPath("$[*].arrivalTime").exists()).andExpect(jsonPath("$[*].arrivalDate").exists())
                    .andExpect(jsonPath("$[*].travelTime").exists())
                    .andExpect(jsonPath("$[*].carrier").exists())
                    .andReturn();

            Assertions.assertTrue(checkSort(SortCriteria.PRICE, SortType.ASC, result));
        }

        @Test
        @SneakyThrows
        @DisplayName("[200] sort by price in desc order with UA language")
        void getSortedTicketsByPriceDescUaIs200() {
            RequestSortedTicketsDto dto = requestTicketsDto;
            dto.setSortingBy(getCriteriaMap(SortCriteria.PRICE, SortType.DESC));
            MvcResult result = mvc.perform(MockMvcRequestBuilders.post(URL + "/sortBy")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(requestTicketsDto))
                            .header(HttpHeaders.CONTENT_LANGUAGE, UA)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(100)))
                    .andExpect(jsonPath("$[*].id").exists())
                    .andExpect(jsonPath("$[*].type").exists())
                    .andExpect(jsonPath("$[*].placeFrom").exists())
                    .andExpect(jsonPath("$[*].placeAt").exists())
                    .andExpect(jsonPath("$[*].departureCity").value(everyItem(is(dto.getDepartureCity()))))
                    .andExpect(jsonPath("$[*].arrivalCity").value(everyItem(is(dto.getArrivalCity()))))
                    .andExpect(jsonPath("$[*].departureTime").exists())
                    .andExpect(jsonPath("$[*].departureDate").value(everyItem(is(TicketMapper.departureTimeMapping(dto.getDepartureDate().toString(), UA)))))

                    .andExpect(jsonPath("$[*].arrivalTime").exists()).andExpect(jsonPath("$[*].arrivalDate").exists())
                    .andExpect(jsonPath("$[*].travelTime").exists())
                    .andExpect(jsonPath("$[*].carrier").exists())
                    .andReturn();

            Assertions.assertTrue(checkSort(SortCriteria.PRICE, SortType.DESC, result));

        }

        @Test
        @SneakyThrows
        @DisplayName("[200] sort by price in desc order with ENG language")
        void getSortedTicketsByPriceDescEngIs200() {
            RequestSortedTicketsDto dto = requestTicketsDto;
            dto.setSortingBy(getCriteriaMap(SortCriteria.PRICE, SortType.DESC));
            MvcResult result = mvc.perform(MockMvcRequestBuilders.post(URL + "/sortBy")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(requestTicketsDto))
                            .header(HttpHeaders.CONTENT_LANGUAGE, ENG)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(100)))
                    .andExpect(jsonPath("$[*].id").exists())
                    .andExpect(jsonPath("$[*].type").exists())
                    .andExpect(jsonPath("$[*].placeFrom").exists())
                    .andExpect(jsonPath("$[*].placeAt").exists())
                    .andExpect(jsonPath("$[*].departureCity").value(everyItem(is(dto.getDepartureCity()))))
                    .andExpect(jsonPath("$[*].arrivalCity").value(everyItem(is(dto.getArrivalCity()))))
                    .andExpect(jsonPath("$[*].departureTime").exists())
                    .andExpect(jsonPath("$[*].departureDate").value(everyItem(is(TicketMapper.departureTimeMapping(dto.getDepartureDate().toString(), ENG)))))

                    .andExpect(jsonPath("$[*].arrivalTime").exists()).andExpect(jsonPath("$[*].arrivalDate").exists())
                    .andExpect(jsonPath("$[*].travelTime").exists())
                    .andExpect(jsonPath("$[*].carrier").exists())
                    .andReturn();

            Assertions.assertTrue(checkSort(SortCriteria.PRICE, SortType.DESC, result));

        }

        @Test
        @SneakyThrows
        @DisplayName("[200] sort by arrival time in asc order with UA language")
        void getSortedTicketsByArrivalTimeAscUaIs200() {
            RequestSortedTicketsDto dto = requestTicketsDto;
            dto.setSortingBy(getCriteriaMap(SortCriteria.ARRIVAL_TIME, SortType.ASC));
            MvcResult result = mvc.perform(MockMvcRequestBuilders.post(URL + "/sortBy")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(requestTicketsDto))
                            .header(HttpHeaders.CONTENT_LANGUAGE, UA)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(100)))
                    .andExpect(jsonPath("$[*].id").exists())
                    .andExpect(jsonPath("$[*].type").exists())
                    .andExpect(jsonPath("$[*].placeFrom").exists())
                    .andExpect(jsonPath("$[*].placeAt").exists())
                    .andExpect(jsonPath("$[*].departureCity").value(everyItem(is(dto.getDepartureCity()))))
                    .andExpect(jsonPath("$[*].arrivalCity").value(everyItem(is(dto.getArrivalCity()))))
                    .andExpect(jsonPath("$[*].departureTime").exists())
                    .andExpect(jsonPath("$[*].departureDate").value(everyItem(is(TicketMapper.departureTimeMapping(dto.getDepartureDate().toString(), UA)))))
                    .andExpect(jsonPath("$[*].arrivalTime").exists()).andExpect(jsonPath("$[*].arrivalDate").exists())
                    .andExpect(jsonPath("$[*].travelTime").exists())
                    .andExpect(jsonPath("$[*].price").exists())
                    .andExpect(jsonPath("$[*].carrier").exists())
                    .andReturn();

            Assertions.assertTrue(checkSort(SortCriteria.ARRIVAL_TIME, SortType.ASC, result));

        }

        @Test
        @SneakyThrows
        @DisplayName("[200] sort by arrival time in asc order with ENG language")
        void getSortedTicketsByArrivalTimeAscEngIs200() {
            RequestSortedTicketsDto dto = requestTicketsDto;
            dto.setSortingBy(getCriteriaMap(SortCriteria.ARRIVAL_TIME, SortType.ASC));
            mvc.perform(MockMvcRequestBuilders.post(URL + "/sortBy")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(requestTicketsDto))
                            .header(HttpHeaders.CONTENT_LANGUAGE, ENG)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].arrivalTime").value(Matchers.lessThan("$[99].arrivalTime")))
                    .andExpect(jsonPath("$", hasSize(100)))
                    .andExpect(jsonPath("$[*].id").exists())
                    .andExpect(jsonPath("$[*].type").exists())
                    .andExpect(jsonPath("$[*].placeFrom").exists())
                    .andExpect(jsonPath("$[*].placeAt").exists())
                    .andExpect(jsonPath("$[*].departureCity").value(everyItem(is(requestTicketsDto.getDepartureCity()))))
                    .andExpect(jsonPath("$[*].arrivalCity").value(everyItem(is(requestTicketsDto.getArrivalCity()))))
                    .andExpect(jsonPath("$[*].departureTime").exists())
                    .andExpect(jsonPath("$[*].departureDate").value(everyItem(is(TicketMapper.departureTimeMapping(requestTicketsDto.getDepartureDate().toString(), ENG)))))
                    .andExpect(jsonPath("$[*].arrivalTime").exists()).andExpect(jsonPath("$[*].arrivalDate").exists())
                    .andExpect(jsonPath("$[*].travelTime").exists())
                    .andExpect(jsonPath("$[*].price").exists())
                    .andExpect(jsonPath("$[*].carrier").exists());

        }

        @Test
        @SneakyThrows
        @DisplayName("[200] sort by arrival time in desc order with UA language")
        void getSortedTicketsByArrivalTimeDescUaIs200() {
            RequestSortedTicketsDto dto = requestTicketsDto;
            dto.setSortingBy(getCriteriaMap(SortCriteria.ARRIVAL_TIME, SortType.DESC));
            mvc.perform(MockMvcRequestBuilders.post(URL + "/sortBy")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(requestTicketsDto))
                            .header(HttpHeaders.CONTENT_LANGUAGE, UA)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[99].arrivalTime").value(Matchers.lessThan("$[0].arrivalTime")))
                    .andExpect(jsonPath("$", hasSize(100)))
                    .andExpect(jsonPath("$[*].id").exists())
                    .andExpect(jsonPath("$[*].type").exists())
                    .andExpect(jsonPath("$[*].placeFrom").exists())
                    .andExpect(jsonPath("$[*].placeAt").exists())
                    .andExpect(jsonPath("$[*].departureCity").value(everyItem(is(requestTicketsDto.getDepartureCity()))))
                    .andExpect(jsonPath("$[*].arrivalCity").value(everyItem(is(requestTicketsDto.getArrivalCity()))))
                    .andExpect(jsonPath("$[*].departureTime").exists())
                    .andExpect(jsonPath("$[*].departureDate").value(everyItem(is(TicketMapper.departureTimeMapping(requestTicketsDto.getDepartureDate().toString(), UA)))))
                    .andExpect(jsonPath("$[*].arrivalTime").exists()).andExpect(jsonPath("$[*].arrivalDate").exists())
                    .andExpect(jsonPath("$[*].travelTime").exists())
                    .andExpect(jsonPath("$[*].price").exists())
                    .andExpect(jsonPath("$[*].carrier").exists());

        }

        @Test
        @SneakyThrows
        @DisplayName("[200] sort by arrival time in desc order with ENG language")
        void getSortedTicketsByArrivalTimeDescEngIs200() {
            RequestSortedTicketsDto dto = requestTicketsDto;
            dto.setSortingBy(getCriteriaMap(SortCriteria.ARRIVAL_TIME, SortType.DESC));
            mvc.perform(MockMvcRequestBuilders.post(URL + "/sortBy")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(requestTicketsDto))
                            .header(HttpHeaders.CONTENT_LANGUAGE, ENG)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[99].arrivalTime").value(Matchers.lessThan("$[0].arrivalTime")))
                    .andExpect(jsonPath("$", hasSize(100)))
                    .andExpect(jsonPath("$[*].id").exists())
                    .andExpect(jsonPath("$[*].type").exists())
                    .andExpect(jsonPath("$[*].placeFrom").exists())
                    .andExpect(jsonPath("$[*].placeAt").exists())
                    .andExpect(jsonPath("$[*].departureCity").value(everyItem(is(requestTicketsDto.getDepartureCity()))))
                    .andExpect(jsonPath("$[*].arrivalCity").value(everyItem(is(requestTicketsDto.getArrivalCity()))))
                    .andExpect(jsonPath("$[*].departureTime").exists())
                    .andExpect(jsonPath("$[*].departureDate").value(everyItem(is(TicketMapper.departureTimeMapping(requestTicketsDto.getDepartureDate().toString(), ENG)))))
                    .andExpect(jsonPath("$[*].arrivalTime").exists()).andExpect(jsonPath("$[*].arrivalDate").exists())
                    .andExpect(jsonPath("$[*].travelTime").exists())
                    .andExpect(jsonPath("$[*].price").exists())
                    .andExpect(jsonPath("$[*].carrier").exists());

        }

        @Test
        @SneakyThrows
        @DisplayName("[200] sort by departure time in asc order with UA language")
        void getSortedTicketsByDepartureTimeAscUaIs200() {
            RequestSortedTicketsDto dto = requestTicketsDto;
            dto.setSortingBy(getCriteriaMap(SortCriteria.DEPARTURE_TIME, SortType.ASC));
            mvc.perform(MockMvcRequestBuilders.post(URL + "/sortBy")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(requestTicketsDto))
                            .header(HttpHeaders.CONTENT_LANGUAGE, UA)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].departureTime").value(Matchers.lessThan("$[99].departureTime")))
                    .andExpect(jsonPath("$", hasSize(100)))
                    .andExpect(jsonPath("$[*].id").exists())
                    .andExpect(jsonPath("$[*].type").exists())
                    .andExpect(jsonPath("$[*].placeFrom").exists())
                    .andExpect(jsonPath("$[*].placeAt").exists())
                    .andExpect(jsonPath("$[*].departureCity").value(everyItem(is(requestTicketsDto.getDepartureCity()))))
                    .andExpect(jsonPath("$[*].arrivalCity").value(everyItem(is(requestTicketsDto.getArrivalCity()))))
                    .andExpect(jsonPath("$[*].departureDate").value(everyItem(is(TicketMapper.departureTimeMapping(requestTicketsDto.getDepartureDate().toString(), UA)))))

                    .andExpect(jsonPath("$[*].arrivalTime").exists()).andExpect(jsonPath("$[*].arrivalDate").exists())
                    .andExpect(jsonPath("$[*].travelTime").exists())
                    .andExpect(jsonPath("$[*].price").exists())
                    .andExpect(jsonPath("$[*].carrier").exists());

        }

        @Test
        @SneakyThrows
        @DisplayName("[200] sort by departure time in asc order with ENG language")
        void getSortedTicketsByDepartureTimeAscEngIs200() {
            RequestSortedTicketsDto dto = requestTicketsDto;
            dto.setSortingBy(getCriteriaMap(SortCriteria.DEPARTURE_TIME, SortType.ASC));
            mvc.perform(MockMvcRequestBuilders.post(URL + "/sortBy")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(requestTicketsDto))
                            .header(HttpHeaders.CONTENT_LANGUAGE, ENG)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].departureTime").value(Matchers.lessThan("$[99].departureTime")))
                    .andExpect(jsonPath("$", hasSize(100)))
                    .andExpect(jsonPath("$[*].id").exists())
                    .andExpect(jsonPath("$[*].type").exists())
                    .andExpect(jsonPath("$[*].placeFrom").exists())
                    .andExpect(jsonPath("$[*].placeAt").exists())
                    .andExpect(jsonPath("$[*].departureCity").value(everyItem(is(requestTicketsDto.getDepartureCity()))))
                    .andExpect(jsonPath("$[*].arrivalCity").value(everyItem(is(requestTicketsDto.getArrivalCity()))))
                    .andExpect(jsonPath("$[*].departureDate").value(everyItem(is(TicketMapper.departureTimeMapping(requestTicketsDto.getDepartureDate().toString(), ENG)))))

                    .andExpect(jsonPath("$[*].arrivalTime").exists()).andExpect(jsonPath("$[*].arrivalDate").exists())
                    .andExpect(jsonPath("$[*].travelTime").exists())
                    .andExpect(jsonPath("$[*].price").exists())
                    .andExpect(jsonPath("$[*].carrier").exists());

        }

        @Test
        @SneakyThrows
        @DisplayName("[200] sort by departure time in desc order with UA language")
        void getSortedTicketsByDepartureTimeDescUaIs200() {
            RequestSortedTicketsDto dto = requestTicketsDto;
            dto.setSortingBy(getCriteriaMap(SortCriteria.DEPARTURE_TIME, SortType.DESC));
            mvc.perform(MockMvcRequestBuilders.post(URL + "/sortBy")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(requestTicketsDto))
                            .header(HttpHeaders.CONTENT_LANGUAGE, UA)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[99].departureTime").value(Matchers.lessThan("$[0].departureTime")))
                    .andExpect(jsonPath("$", hasSize(100)))
                    .andExpect(jsonPath("$[*].id").exists())
                    .andExpect(jsonPath("$[*].type").exists())
                    .andExpect(jsonPath("$[*].placeFrom").exists())
                    .andExpect(jsonPath("$[*].placeAt").exists())
                    .andExpect(jsonPath("$[*].departureCity").value(everyItem(is(requestTicketsDto.getDepartureCity()))))
                    .andExpect(jsonPath("$[*].arrivalCity").value(everyItem(is(requestTicketsDto.getArrivalCity()))))
                    .andExpect(jsonPath("$[*].departureDate").value(everyItem(is(TicketMapper.departureTimeMapping(requestTicketsDto.getDepartureDate().toString(), UA)))))

                    .andExpect(jsonPath("$[*].arrivalTime").exists()).andExpect(jsonPath("$[*].arrivalDate").exists())
                    .andExpect(jsonPath("$[*].travelTime").exists())
                    .andExpect(jsonPath("$[*].price").exists())
                    .andExpect(jsonPath("$[*].carrier").exists());

        }

        @Test
        @SneakyThrows
        @DisplayName("[200] sort by departure time in desc order with ENG language")
        void getSortedTicketsByDepartureTimeDescEngIs200() {
            RequestSortedTicketsDto dto = requestTicketsDto;
            dto.setSortingBy(getCriteriaMap(SortCriteria.DEPARTURE_TIME, SortType.DESC));
            mvc.perform(MockMvcRequestBuilders.post(URL + "/sortBy")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(requestTicketsDto))
                            .header(HttpHeaders.CONTENT_LANGUAGE, ENG)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[99].departureTime").value(Matchers.lessThan("$[0].departureTime")))
                    .andExpect(jsonPath("$", hasSize(100)))
                    .andExpect(jsonPath("$[*].id").exists())
                    .andExpect(jsonPath("$[*].type").exists())
                    .andExpect(jsonPath("$[*].placeFrom").exists())
                    .andExpect(jsonPath("$[*].placeAt").exists())
                    .andExpect(jsonPath("$[*].departureCity").value(everyItem(is(requestTicketsDto.getDepartureCity()))))
                    .andExpect(jsonPath("$[*].arrivalCity").value(everyItem(is(requestTicketsDto.getArrivalCity()))))
                    .andExpect(jsonPath("$[*].departureDate").value(everyItem(is(TicketMapper.departureTimeMapping(requestTicketsDto.getDepartureDate().toString(), ENG)))))

                    .andExpect(jsonPath("$[*].arrivalTime").exists()).andExpect(jsonPath("$[*].arrivalDate").exists())
                    .andExpect(jsonPath("$[*].travelTime").exists())
                    .andExpect(jsonPath("$[*].price").exists())
                    .andExpect(jsonPath("$[*].carrier").exists());

        }

        @Test
        @SneakyThrows
        @DisplayName("[200] sort by travel time in asc order with UA language")
        void getSortedTicketsByTravelTimeAscUaIs200() {
            RequestSortedTicketsDto dto = requestTicketsDto;
            dto.setSortingBy(getCriteriaMap(SortCriteria.TRAVEL_TIME, SortType.ASC));
            mvc.perform(MockMvcRequestBuilders.post(URL + "/sortBy")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(requestTicketsDto))
                            .header(HttpHeaders.CONTENT_LANGUAGE, UA)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].travelTime").value(Matchers.lessThan("$[99].travelTime")))
                    .andExpect(jsonPath("$", hasSize(100)))
                    .andExpect(jsonPath("$[*].id").exists())
                    .andExpect(jsonPath("$[*].type").exists())
                    .andExpect(jsonPath("$[*].placeFrom").exists())
                    .andExpect(jsonPath("$[*].placeAt").exists())
                    .andExpect(jsonPath("$[*].departureCity").value(everyItem(is(requestTicketsDto.getDepartureCity()))))
                    .andExpect(jsonPath("$[*].arrivalCity").value(everyItem(is(requestTicketsDto.getArrivalCity()))))
                    .andExpect(jsonPath("$[*].departureDate").value(everyItem(is(TicketMapper.departureTimeMapping(requestTicketsDto.getDepartureDate().toString(), UA)))))
                    .andExpect(jsonPath("$[*].departureTime").exists())
                    .andExpect(jsonPath("$[*].arrivalTime").exists()).andExpect(jsonPath("$[*].arrivalDate").exists())

                    .andExpect(jsonPath("$[*].price").exists())
                    .andExpect(jsonPath("$[*].carrier").exists());

        }

        @Test
        @SneakyThrows
        @DisplayName("[200] sort by travel time in asc order with ENG language")
        void getSortedTicketsByTravelTimeAscEngIs200() {
            RequestSortedTicketsDto dto = requestTicketsDto;
            dto.setSortingBy(getCriteriaMap(SortCriteria.TRAVEL_TIME, SortType.ASC));
            mvc.perform(MockMvcRequestBuilders.post(URL + "/sortBy")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(requestTicketsDto))
                            .header(HttpHeaders.CONTENT_LANGUAGE, ENG)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].travelTime").value(Matchers.lessThan("$[99].travelTime")))
                    .andExpect(jsonPath("$", hasSize(100)))
                    .andExpect(jsonPath("$[*].id").exists())
                    .andExpect(jsonPath("$[*].type").exists())
                    .andExpect(jsonPath("$[*].placeFrom").exists())
                    .andExpect(jsonPath("$[*].placeAt").exists())
                    .andExpect(jsonPath("$[*].departureCity").value(everyItem(is(requestTicketsDto.getDepartureCity()))))
                    .andExpect(jsonPath("$[*].arrivalCity").value(everyItem(is(requestTicketsDto.getArrivalCity()))))
                    .andExpect(jsonPath("$[*].departureDate").value(everyItem(is(TicketMapper.departureTimeMapping(requestTicketsDto.getDepartureDate().toString(), ENG)))))
                    .andExpect(jsonPath("$[*].departureTime").exists())
                    .andExpect(jsonPath("$[*].arrivalTime").exists()).andExpect(jsonPath("$[*].arrivalDate").exists())
                    
                    .andExpect(jsonPath("$[*].price").exists())
                    .andExpect(jsonPath("$[*].carrier").exists());

        }

        @Test
        @SneakyThrows
        @DisplayName("[200] sort by travel time in desc order with UA language")
        void getSortedTicketsByTravelTimeDescUaIs200() {
            RequestSortedTicketsDto dto = requestTicketsDto;
            dto.setSortingBy(getCriteriaMap(SortCriteria.ARRIVAL_TIME, SortType.DESC));
            mvc.perform(MockMvcRequestBuilders.post(URL + "/sortBy")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(requestTicketsDto))
                            .header(HttpHeaders.CONTENT_LANGUAGE, UA)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[99].travelTime").value(Matchers.lessThan("$[0].travelTime")))
                    .andExpect(jsonPath("$", hasSize(100)))
                    .andExpect(jsonPath("$[*].id").exists())
                    .andExpect(jsonPath("$[*].type").exists())
                    .andExpect(jsonPath("$[*].placeFrom").exists())
                    .andExpect(jsonPath("$[*].placeAt").exists())
                    .andExpect(jsonPath("$[*].departureCity").value(everyItem(is(requestTicketsDto.getDepartureCity()))))
                    .andExpect(jsonPath("$[*].arrivalCity").value(everyItem(is(requestTicketsDto.getArrivalCity()))))
                    .andExpect(jsonPath("$[*].departureDate").value(everyItem(is(TicketMapper.departureTimeMapping(requestTicketsDto.getDepartureDate().toString(), UA)))))
                    .andExpect(jsonPath("$[*].departureTime").exists())
                    .andExpect(jsonPath("$[*].arrivalTime").exists()).andExpect(jsonPath("$[*].arrivalDate").exists())

                    .andExpect(jsonPath("$[*].price").exists())
                    .andExpect(jsonPath("$[*].carrier").exists());

        }

        @Test
        @SneakyThrows
        @DisplayName("[200] sort by travel time in desc order with ENG language")
        void getSortedTicketsByTravelTimeDescEngIs200() {
            RequestSortedTicketsDto dto = requestTicketsDto;
            dto.setSortingBy(getCriteriaMap(SortCriteria.ARRIVAL_TIME, SortType.DESC));
            mvc.perform(MockMvcRequestBuilders.post(URL + "/sortBy")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(requestTicketsDto))
                            .header(HttpHeaders.CONTENT_LANGUAGE, ENG)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[99].travelTime").value(Matchers.lessThan("$[0].travelTime")))
                    .andExpect(jsonPath("$", hasSize(100)))
                    .andExpect(jsonPath("$[*].id").exists())
                    .andExpect(jsonPath("$[*].type").exists())
                    .andExpect(jsonPath("$[*].placeFrom").exists())
                    .andExpect(jsonPath("$[*].placeAt").exists())
                    .andExpect(jsonPath("$[*].departureCity").value(everyItem(is(requestTicketsDto.getDepartureCity()))))
                    .andExpect(jsonPath("$[*].arrivalCity").value(everyItem(is(requestTicketsDto.getArrivalCity()))))
                    .andExpect(jsonPath("$[*].departureDate").value(everyItem(is(TicketMapper.departureTimeMapping(requestTicketsDto.getDepartureDate().toString(), ENG)))))
                    .andExpect(jsonPath("$[*].departureTime").exists())
                    .andExpect(jsonPath("$[*].arrivalTime").exists()).andExpect(jsonPath("$[*].arrivalDate").exists())
                    
                    .andExpect(jsonPath("$[*].price").exists())
                    .andExpect(jsonPath("$[*].carrier").exists());

        }
    }

}
