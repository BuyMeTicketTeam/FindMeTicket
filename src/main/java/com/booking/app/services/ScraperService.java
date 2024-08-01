package com.booking.app.services;

import com.booking.app.entities.ticket.Route;
import com.booking.app.entities.ticket.Ticket;
import org.apache.commons.lang.mutable.MutableBoolean;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.text.ParseException;
import java.util.concurrent.CompletableFuture;

public interface ScraperService {

    CompletableFuture<Boolean> scrapeTickets(SseEmitter emitter, Route route, String language, Boolean doSend, MutableBoolean emitterNotExpired);

    CompletableFuture<Boolean> scrapeTicketUri(SseEmitter emitter, Ticket ticket, String language, MutableBoolean emitterNotExpired) throws IOException, ParseException;

}