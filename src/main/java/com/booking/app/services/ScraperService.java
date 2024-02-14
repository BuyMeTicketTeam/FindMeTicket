package com.booking.app.services;

import com.booking.app.dto.RequestTicketsDTO;
import com.booking.app.entity.BusTicket;
import com.booking.app.entity.Route;
import com.booking.app.entity.Ticket;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.text.ParseException;
import java.util.concurrent.CompletableFuture;

public interface ScraperService {
    CompletableFuture<Boolean> scrapeTickets(SseEmitter emitter, Route route, String language) throws ParseException, IOException;

    CompletableFuture<Boolean> getBusTicket(SseEmitter emitter, BusTicket ticket, String language) throws IOException, ParseException;

    String determineBaseUrl(String language);
}
