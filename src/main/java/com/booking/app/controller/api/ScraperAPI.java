package com.booking.app.controller.api;

import com.booking.app.dto.RequestSortedTicketsDTO;
import com.booking.app.dto.RequestTicketsDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.text.ParseException;
import java.util.UUID;

public interface ScraperAPI {

    @Operation(summary = "Searching tickets", description = "Find tickets based by criteria")
    @ApiResponse(responseCode = "200", description = "Returns a list of cities if found")
    SseEmitter findTickets(@RequestBody RequestTicketsDTO ticketsDTO) throws IOException, ParseException, InterruptedException;

    @Operation(summary = "Single ticket", description = "Ticket by ID")
    @ApiResponse(responseCode = "200", description = "Returns ticket if found")
    SseEmitter getTicket(@PathVariable UUID id) throws IOException, ParseException;

    @Operation(summary = "Sorting", description = "Either by price, travel time, departure, or arrival")
    @ApiResponse(responseCode = "200", description = "Returns sorted tickets")
    ResponseEntity<?> getSortedTickets(@RequestBody RequestSortedTicketsDTO requestSortedTicketsDTO);
}
