package com.booking.app.services.impl;

import com.booking.app.dto.RequestTicketsDTO;
import com.booking.app.dto.UrlAndPriceDTO;
import com.booking.app.entity.BusTicket;
import com.booking.app.entity.Route;
import com.booking.app.entity.Ticket;
import com.booking.app.entity.TrainTicket;
import com.booking.app.exception.exception.ResourceNotFoundException;
import com.booking.app.mapper.BusMapper;
import com.booking.app.mapper.TrainMapper;
import com.booking.app.repositories.BusTicketRepository;
import com.booking.app.repositories.RouteRepository;
import com.booking.app.repositories.TrainTicketRepository;
import com.booking.app.services.ScraperService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

import static com.booking.app.constant.SiteConstants.*;

@Service
@RequiredArgsConstructor
@Log4j2
public class ScraperServiceImpl {

    private final TrainTicketRepository trainTicketRepository;

    private final ScraperService busfor;

    private final ScraperService infobus;

    private final ScraperService proizd;

    private final ScraperService train;

    private final RouteRepository routeRepository;

    private final BusTicketRepository busTicketRepository;

    private final BusMapper busMapper;

    private final TrainMapper trainMapper;

    @Async
    public CompletableFuture<Boolean> scrapeTickets(RequestTicketsDTO requestTicketDTO, SseEmitter emitter, String language) throws IOException, ParseException {
        Route route = routeRepository.findByDepartureCityAndArrivalCityAndDepartureDate(requestTicketDTO.getDepartureCity(), requestTicketDTO.getArrivalCity(), requestTicketDTO.getDepartureDate());

        Route newRoute = null;
        if (route == null) {
            newRoute = createRoute(requestTicketDTO);

            List<CompletableFuture<Boolean>> completableFutureListBus = completableFutureListBuses(emitter, newRoute, language, requestTicketDTO.getBus(), requestTicketDTO.getTrain());
            CompletableFuture<Void> allOf = CompletableFuture.allOf(completableFutureListBus.toArray(CompletableFuture[]::new));
            try {
                allOf.join();
            } catch (CancellationException | CompletionException e) {
                log.error("Error in Scraper service, scrapeTickets() method: " + e.getMessage());
                emitter.completeWithError(e);
                return CompletableFuture.completedFuture(false);
            }
            if (completableFutureListBus.stream().anyMatch(t -> {
                try {
                    return t.get().equals(true);
                } catch (ExecutionException | InterruptedException e) {
                    log.warn("Interrupted while waiting for CompletableFuture result.", e);
                    /* Clean up whatever needs to be handled before interrupting  */
                    Thread.currentThread().interrupt();
                    return false;
                }
            })) {
                routeRepository.save(newRoute);
            }
            emitter.complete();

        } else {
            for (Ticket ticket : route.getTickets()) {
                if (ticket instanceof BusTicket && requestTicketDTO.getBus()) {
                    emitter.send(SseEmitter.event().name("ticket data: ").data(busMapper.ticketToTicketDto((BusTicket) ticket, language)));
                }
                if (ticket instanceof TrainTicket && requestTicketDTO.getTrain()) {
                    emitter.send(SseEmitter.event().name("ticket data: ").data(trainMapper.toTrainTicketDto((TrainTicket) ticket, language)));
                }
            }
        }

        emitter.complete();

        return route == null && newRoute.getTickets().isEmpty() ?
                CompletableFuture.completedFuture(false) : CompletableFuture.completedFuture(true);
    }

    @Async
    public CompletableFuture<Boolean> getTicket(UUID id, SseEmitter emitter, String language) throws IOException, ParseException {
        try {
            tryBus(id, emitter, language);
            return CompletableFuture.completedFuture(true);
        } catch (ResourceNotFoundException busEx) {
            try {
                tryTrain(id, emitter, language);
                return CompletableFuture.completedFuture(true);
            } catch (ResourceNotFoundException e) {
                emitter.completeWithError(e);
                return CompletableFuture.completedFuture(false);
            }
        }
    }

    private void tryTrain(UUID id, SseEmitter emitter, String language) throws IOException {
        TrainTicket trainTicket = trainTicketRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(String.format("No present by %s ID", id)));

        emitter.send(SseEmitter.event().name("ticket info").data(trainMapper.toTrainTicketDto(trainTicket, language)));

        trainTicket.getInfoList().forEach(t -> {
            try {
                emitter.send(SseEmitter.event().name(PROIZD_UA).data(trainMapper.toTrainComfortInfoDTO(t)));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        emitter.complete();
    }

    private void tryBus(UUID id, SseEmitter emitter, String language) throws IOException, ParseException {

        BusTicket busTicket;
        busTicket = busTicketRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(String.format("No BUS busTicket present by %s ID", id)));

        emitter.send(SseEmitter.event().name("busTicket info").data(busMapper.ticketToTicketDto(busTicket, language)));

        if (!busTicket.linksAreScraped()) {

            List<CompletableFuture<Boolean>> completableFutureListBus = new LinkedList<>();

            if (busTicket.getBusforPrice() != null)
                completableFutureListBus.add(busfor.getBusTicket(emitter, busTicket, language));
            if (busTicket.getInfobusPrice() != null)
                completableFutureListBus.add(infobus.getBusTicket(emitter, busTicket, language));
            if (busTicket.getProizdPrice() != null)
                completableFutureListBus.add(proizd.getBusTicket(emitter, busTicket, language));

            CompletableFuture<Void> allOf = CompletableFuture.allOf(completableFutureListBus.toArray((CompletableFuture[]::new)));

            try {
                allOf.join();
            } catch (CancellationException | CompletionException e) {
                log.error("Error in Scraper service, scrapeTickets() method: " + e.getMessage());
                emitter.completeWithError(e);
                return;
            }

            busTicketRepository.save(busTicket);
        } else {
            if (busTicket.getProizdLink() != null) {
                emitter.send(SseEmitter.event().name(PROIZD_UA).data(UrlAndPriceDTO.builder()
                        .price(busTicket.getProizdPrice())
                        .url(busTicket.getProizdLink())
                        .build()));
                log.info("PROIZD URL IN single getTicket() INSTANTLY:" + busTicket.getProizdLink());
            }
            if (busTicket.getBusforLink() != null) {
                emitter.send(SseEmitter.event().name(BUSFOR_UA).data(UrlAndPriceDTO.builder()
                        .price(busTicket.getBusforPrice())
                        .url(busTicket.getBusforLink())
                        .build()));
                log.info("BUSFOR URL IN single getTicket() INSTANTLY: " + busTicket.getBusforLink());
            }
            if (busTicket.getInfobusLink() != null) {
                emitter.send(SseEmitter.event().name(INFOBUS).data(
                        UrlAndPriceDTO.builder()
                                .price(busTicket.getInfobusPrice())
                                .url(busTicket.getInfobusLink())
                                .build()));
                log.info("INFOBUS URL IN single getTicket() INSTANTLY:  " + busTicket.getInfobusLink());
            }

        }
        emitter.complete();
    }

    private static Route createRoute(RequestTicketsDTO requestTicketDTO) {
        return Route.builder()
                .addingTime(LocalDateTime.now())
                .departureCity(requestTicketDTO.getDepartureCity())
                .arrivalCity(requestTicketDTO.getArrivalCity())
                .departureDate(requestTicketDTO.getDepartureDate())
                .build();
    }

    private List<CompletableFuture<Boolean>> completableFutureListBuses(SseEmitter emitter, Route newRoute, String language, Boolean doBus, Boolean doTrain) throws ParseException, IOException {
        return Arrays.asList(
                infobus.scrapeTickets(emitter, newRoute, language, doBus),
                proizd.scrapeTickets(emitter, newRoute, language, doBus),
                busfor.scrapeTickets(emitter, newRoute, language, doBus),
                train.scrapeTickets(emitter, newRoute, language, doTrain)
        );
    }

}
