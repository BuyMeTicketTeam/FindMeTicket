package com.booking.app.controller.api;

import com.booking.app.dto.RequestSortedTicketsDTO;
import com.booking.app.dto.RequestTicketsDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.io.IOException;
import java.text.ParseException;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public interface ScraperAPI {

    @Operation(summary = "Searching tickets", description = "Find tickets based by criteria")
    @ApiResponse(responseCode = "200", description = "Returns a list of cities if found")
    ResponseEntity<ResponseBodyEmitter> findTickets(@RequestBody RequestTicketsDTO ticketsDTO, HttpServletRequest request) throws IOException, ParseException, InterruptedException, ExecutionException;

    @Operation(summary = "Single ticket", description = "Ticket by ID")
    @ApiResponse(responseCode = "200", description = "Returns ticket if found")
    ResponseEntity<ResponseBodyEmitter> getTicket(@PathVariable UUID id, HttpServletRequest request) throws IOException, ParseException;

    @Operation(summary = "Sorting", description = "Either by price, travel time, departure, or arrival")
    @ApiResponse(responseCode = "200", description = "Returns sorted tickets")
    ResponseEntity<?> getSortedTickets(@RequestBody RequestSortedTicketsDTO requestSortedTicketsDTO, HttpServletRequest request);
}
