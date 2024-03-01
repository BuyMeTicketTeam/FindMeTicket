package com.booking.app.services;

import com.booking.app.entity.Route;
import com.booking.app.entity.Ticket;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

public interface TicketOperation {
    void saveTicket(SseEmitter emitter, Route route, String language, Boolean doDisplay, Ticket scrapedTicket) throws IOException;

    void updateTicket(Ticket ticket, Ticket scrapedTicket);

}
