package com.booking.app.controllers.testUtils;

import com.booking.app.constants.Website;
import com.booking.app.entities.ticket.Route;
import com.booking.app.entities.ticket.bus.BusInfo;
import com.booking.app.entities.ticket.bus.BusTicket;
import com.booking.app.entities.ticket.train.TrainInfo;
import com.booking.app.entities.ticket.train.TrainTicket;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang.RandomStringUtils;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@UtilityClass
public class TicketUtils {

    public BusTicket generateMockBusTicket(Route route, LocalTime departureTime, Instant arrivalDateTime, LocalDate departureDate) {
        return BusTicket.builder()
                .id(UUID.randomUUID())
                .route(route)
                .placeAt(RandomStringUtils.randomAlphabetic(20))
                .placeFrom(RandomStringUtils.randomAlphabetic(20))
                .departureTime(departureTime)
                .arrivalDateTime(arrivalDateTime)
                .travelTime(DateTimeUtils.countTravelTime(departureDate, departureTime, arrivalDateTime))
                .carrier(RandomStringUtils.randomAlphabetic(10))
                .build();
    }

    public TrainTicket generateMockTrainTicket(Route route, LocalTime departureTime, Instant arrivalDateTime, LocalDate departureDate) {
        return TrainTicket.builder()
                .id(UUID.randomUUID())
                .route(route)
                .placeAt(RandomStringUtils.randomAlphabetic(20))
                .placeFrom(RandomStringUtils.randomAlphabetic(20))
                .departureTime(departureTime)
                .arrivalDateTime(arrivalDateTime)
                .travelTime(DateTimeUtils.countTravelTime(departureDate, departureTime, arrivalDateTime))
                .carrier(RandomStringUtils.randomAlphabetic(10))
                .build();
    }

    public List<BusInfo> generateBusInfo(BusTicket busTicket) {
        int numberOfInfoBuses = new Random().nextInt(1, 4);
        List<BusInfo> busInfos = new ArrayList<>();

        for (int i = 0; i < numberOfInfoBuses; i++) {
            busInfos.add(BusInfo.builder()
                    .id(UUID.randomUUID())
                    .price(new BigDecimal(new Random().nextInt(100, 5000)))
                    .link(RandomStringUtils.randomAlphabetic(100))
                    .website(Website.PROIZD)
                    .busTicket(busTicket)
                    .build());
        }

        return busInfos;
    }

    public List<TrainInfo> generateTrainInfo(TrainTicket trainTicket) {
        int numberOfInfoBuses = new Random().nextInt(1, 4);
        List<TrainInfo> trainInfos = new ArrayList<>();

        for (int i = 0; i < numberOfInfoBuses; i++) {
            trainInfos.add(TrainInfo.builder()
                    .id(UUID.randomUUID())
                    .price(new BigDecimal(new Random().nextInt(100, 5000)))
                    .link(RandomStringUtils.randomAlphabetic(100))
                    .comfort(RandomStringUtils.randomAlphabetic(10))
                    .website(Website.INFOBUS)
                    .trainTicket(trainTicket)
                    .build());
        }

        return trainInfos;
    }

}
