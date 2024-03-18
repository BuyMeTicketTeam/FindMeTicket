package com.booking.app.services;

import com.booking.app.entity.BusTicket;
import com.booking.app.entity.Route;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.text.ParseException;
import java.util.concurrent.CompletableFuture;

public interface ScraperService {
    CompletableFuture<Boolean> scrapeTickets(SseEmitter emitter, Route route, String language, Boolean doSend) throws ParseException, IOException;

    CompletableFuture<Boolean> scrapeTicketUri(SseEmitter emitter, BusTicket ticket, String language) throws IOException, ParseException;

}
