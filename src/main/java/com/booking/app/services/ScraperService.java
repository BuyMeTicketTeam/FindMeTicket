package com.booking.app.services;

import com.booking.app.dto.RequestTicketsDTO;
import com.booking.app.entity.Route;
import com.booking.app.entity.Ticket;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.text.ParseException;
import java.util.concurrent.CompletableFuture;

public interface ScraperService {
    CompletableFuture<Boolean> scrapeTickets(RequestTicketsDTO requestTicketDTO, SseEmitter emitter, Route route, String language) throws ParseException, IOException;

    CompletableFuture<Boolean> getTicket(SseEmitter emitter, Ticket ticket, String language) throws IOException, ParseException;

    String determineBaseUrl(String language);
}
