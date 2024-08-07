package com.booking.app.controllers;

import com.booking.app.annotations.GlobalApiResponses;
import com.booking.app.constants.ContentLanguage;
import com.booking.app.dto.ErrorDetailsDto;
import com.booking.app.dto.tickets.RequestSortedTicketsDto;
import com.booking.app.dto.tickets.RequestTicketsDto;
import com.booking.app.dto.tickets.ResponseTicketDto;
import com.booking.app.entities.user.User;
import com.booking.app.services.SearchHistoryService;
import com.booking.app.services.TicketService;
import com.booking.app.services.impl.scraper.ScraperManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.mutable.MutableBoolean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static com.booking.app.constants.ApiMessagesConstants.INVALID_CONTENT_LANGUAGE_HEADER_MESSAGE;
import static com.booking.app.constants.ApiMessagesConstants.INVALID_REQUEST_BODY_FORMAT_OR_VALIDATION_ERROR_MESSAGE;

@RestController
@RequestMapping(path = "/tickets", produces = {MediaType.TEXT_EVENT_STREAM_VALUE})
@RequiredArgsConstructor
@Tag(name = "Ticket", description = "Ticket management")
@GlobalApiResponses
public class TicketController {

    private final ScraperManager scraperManager;
    private final TicketService ticketService;
    private final SearchHistoryService searchHistoryService;

    @GetMapping("/search")
    @Operation(summary = "Search tickets",
            description = "Searches tickets by criteria",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tickets has been found", content = @Content(schema = @Schema(implementation = ResponseTicketDto.class), mediaType = MediaType.TEXT_EVENT_STREAM_VALUE)),
                    @ApiResponse(responseCode = "400", description = INVALID_CONTENT_LANGUAGE_HEADER_MESSAGE + " " + INVALID_REQUEST_BODY_FORMAT_OR_VALIDATION_ERROR_MESSAGE, content = {@Content(schema = @Schema(implementation = ErrorDetailsDto.class), mediaType = MediaType.APPLICATION_JSON_VALUE)}),
                    @ApiResponse(responseCode = "404", description = "Tickets are not found", content = @Content(schema = @Schema(hidden = true)))
            })
    public ResponseBodyEmitter findTickets(@RequestBody @NotNull @Valid RequestTicketsDto requestTicketsDto,
                                           @RequestHeader(HttpHeaders.CONTENT_LANGUAGE) @Parameter(required = true, description = "Content Language", schema = @Schema(type = "string", allowableValues = {"eng", "ua"})) ContentLanguage language,
                                           @AuthenticationPrincipal User user,
                                           HttpServletResponse response) throws IOException, ParseException {
        searchHistoryService.addHistory(requestTicketsDto, language.getLanguage(), user);
        SseEmitter emitter = new SseEmitter();
        MutableBoolean emitterNotExpired = new MutableBoolean(true);
        configureEmitter(emitter, emitterNotExpired);
        CompletableFuture<Boolean> isTicketScraped = scraperManager.findTickets(requestTicketsDto, emitter, emitterNotExpired, language.getLanguage());
//        isTicketScraped.exceptionally(ex -> {
//            ExceptionUtils.logException(ex);
//            return null;
//        });
        isTicketScraped.thenAccept(isFound -> {
            if (Boolean.TRUE.equals(isFound)) {
                response.setStatus(HttpStatus.OK.value());
            } else {
                response.setStatus(HttpStatus.NOT_FOUND.value());
            }
        });
        return emitter;
    }

    @GetMapping("/{ticketId}")
    @Operation(summary = "Get ticket info",
            description = "Gets ticket by ID", responses = {
            @ApiResponse(responseCode = "200", description = "Ticket has been found", content = @Content(schema = @Schema(implementation = ResponseTicketDto.class), mediaType = MediaType.TEXT_EVENT_STREAM_VALUE)),
            @ApiResponse(responseCode = "400", description = INVALID_CONTENT_LANGUAGE_HEADER_MESSAGE + " " + INVALID_REQUEST_BODY_FORMAT_OR_VALIDATION_ERROR_MESSAGE, content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "404", description = "Ticket is not found", content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseBodyEmitter getTicketById(@PathVariable("ticketId") UUID id,
                                             @RequestHeader(HttpHeaders.CONTENT_LANGUAGE) @Parameter(required = true, description = "Content Language", schema = @Schema(type = "string", allowableValues = {"eng", "ua"})) ContentLanguage language,
                                             HttpServletResponse response) throws IOException {
        SseEmitter emitter = new SseEmitter();
        MutableBoolean emitterNotExpired = new MutableBoolean(true);
        configureEmitter(emitter, emitterNotExpired);
        CompletableFuture<Boolean> isTicketFound = scraperManager.getTicketInfo(id, emitter, emitterNotExpired, language.getLanguage());

        isTicketFound.thenAccept(isFound -> {
            if (Boolean.TRUE.equals(isFound)) {
                response.setStatus(HttpStatus.OK.value());
            } else {
                response.setStatus(HttpStatus.NOT_FOUND.value());
            }
        });
        return emitter;
    }

    @PostMapping(value = "/sortBy", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Sort tickets",
            description = "Either by price, travel time, departure, or arrival",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Get sorted tickets", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponseTicketDto.class)), mediaType = MediaType.APPLICATION_JSON_VALUE)),
                    @ApiResponse(responseCode = "400", description = INVALID_CONTENT_LANGUAGE_HEADER_MESSAGE + " " + INVALID_REQUEST_BODY_FORMAT_OR_VALIDATION_ERROR_MESSAGE, content = {@Content(schema = @Schema(implementation = ErrorDetailsDto.class), mediaType = MediaType.APPLICATION_JSON_VALUE)})
            })
    public List<ResponseTicketDto> getSortedTickets(@RequestBody @NotNull @Valid RequestSortedTicketsDto requestSortedTicketsDto,
                                                    @RequestHeader(HttpHeaders.CONTENT_LANGUAGE) @Parameter(required = true, description = "Content Language", schema = @Schema(type = "string", allowableValues = {"eng", "ua"})) ContentLanguage language) {
        return ticketService.getSortedTickets(requestSortedTicketsDto, language.getLanguage());
    }

    @PostMapping(value = "/transport", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get tickets", description = "Get tickets by type transport")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully returned"),
            @ApiResponse(responseCode = "404", description = "No tickets of the requested type were found")
    })
    public ResponseEntity<List<ResponseTicketDto>> getTicketByType(@RequestBody @NotNull @Valid RequestTicketsDto requestTicketsDTO) throws IOException {
        return ticketService.getTickets(requestTicketsDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Configures the given {@link SseEmitter} with the provided {@link MutableBoolean} to manage its lifecycle.
     * <p>
     * This method sets up the {@link SseEmitter} to call the provided {@link MutableBoolean} to mark it as expired
     * when the emitter completes, encounters an error, or times out.
     * </p>
     *
     * @param emitter           the {@link SseEmitter} to be configured. This emitter is used to send events to the client.
     *                          It should not be {@code null}.
     * @param emitterNotExpired a {@link MutableBoolean} instance used to track the expiration status of the emitter.
     *                          This should be initialized to {@code true} when creating the emitter.
     *                          It is updated to {@code false} when the emitter completes, encounters an error, or times out.
     * @throws NullPointerException     if {@code emitter} is {@code null}.
     * @throws IllegalArgumentException if {@code emitterNotExpired} is {@code null}.
     */
    private void configureEmitter(SseEmitter emitter, MutableBoolean emitterNotExpired) {
        Runnable expire = () -> emitterNotExpired.setValue(false);
        if (Objects.nonNull(emitter)) {
            emitter.onCompletion(expire);
            emitter.onError(t -> expire.run());
            emitter.onTimeout(expire);
        }
    }


}
