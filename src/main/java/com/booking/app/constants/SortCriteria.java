package com.booking.app.constants;

import lombok.Getter;

@Getter
public enum SortCriteria {

    PRICE("price"),

    DEPARTURE_TIME("departureTime"),

    ARRIVAL_TIME("arrivalTime"),

    TRAVEL_TIME("travelTime");

    private final String criteria;

    SortCriteria(String criteria) {
        this.criteria = criteria;
    }

}
