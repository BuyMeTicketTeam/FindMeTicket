package com.booking.app.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SortCriteriaConstants {
    public static final String PRICE_CRITERIA = "Price";
    public static final String DEPARTURE_TIME_CRITERIA = "DepartureTime";
    public static final String ARRIVAL_TIME_CRITERIA = "ArrivalTime";
    public static final String TRAVEL_TIME_CRITERIA = "TravelTime";
}
