package com.booking.app.exceptions.notfound;

public class TicketNotFoundException extends RuntimeException {
    private static final String MESSAGE = "Ticket with ID %s not found";

    public TicketNotFoundException(String ticketId) {
        super(String.format(MESSAGE, ticketId));
    }
}
