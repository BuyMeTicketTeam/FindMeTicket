package com.booking.app.services;

import java.io.IOException;
import java.text.ParseException;

public interface DeleteTicketService {

    void deleteOldTickets() throws IOException, ParseException;
}
