package com.booking.app.services;

import com.booking.app.entity.ticket.Route;
import com.booking.app.entity.ticket.bus.BusTicket;
import org.apache.commons.lang.mutable.MutableBoolean;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.text.ParseException;
import java.util.concurrent.CompletableFuture;

public interface ScraperService {

    CompletableFuture<Boolean> scrapeTickets(SseEmitter emitter, Route route, String language, Boolean doSend, MutableBoolean emitterNotExpired) throws ParseException, IOException;

    CompletableFuture<Boolean> scrapeTicketUri(SseEmitter emitter, BusTicket ticket, String language, MutableBoolean emitterNotExpired) throws IOException, ParseException;

}