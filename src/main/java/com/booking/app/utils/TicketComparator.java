package com.booking.app.utils;

import com.booking.app.constants.SortCriteria;
import com.booking.app.constants.SortType;
import com.booking.app.dto.RequestSortedTicketsDto;
import com.booking.app.entities.ticket.Ticket;
import com.booking.app.exception.exception.UndefinedSortingCriteriaException;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@UtilityClass
public class TicketComparator {

    private static final Comparator<Ticket> compareByPrice =
            (Ticket t1, Ticket t2) -> (t1.getPrice()).compareTo(t2.getPrice());
    private static final Comparator<Ticket> compareByTravelTime =
            (Ticket t1, Ticket t2) -> (t1.getTravelTime()).compareTo(t2.getTravelTime());
    private static final Comparator<Ticket> compareByDepartureTime =
            (Ticket t1, Ticket t2) -> (t1.getDepartureTime()).compareTo(t2.getDepartureTime());
    private static final Comparator<Ticket> compareByArrivalTime =
            (Ticket t1, Ticket t2) ->
                    (LocalDateTime.ofInstant(t1.getArrivalDateTime(), ZoneId.systemDefault()).toLocalTime().withSecond(0).withNano(0))
                            .compareTo(
                                    LocalDateTime.ofInstant(t2.getArrivalDateTime(), ZoneId.systemDefault()).toLocalTime().withSecond(0).withNano(0));


    public List<Ticket> sort(List<Ticket> tickets, RequestSortedTicketsDto dto) {
        tickets.sort(createCombinedComparator(dto));
        return tickets;
    }

    private Comparator<Ticket> createCombinedComparator(RequestSortedTicketsDto dto) {
        Comparator<Ticket> combinedComparator = null;

        for (Map.Entry<SortCriteria, SortType> entry : dto.getSortingBy().entrySet()) {
            SortCriteria criteria = entry.getKey();
            SortType sortType = entry.getValue();
            Comparator<Ticket> currentComparator = createComparatorForCriteria(criteria, sortType);

            if (combinedComparator == null) {
                combinedComparator = currentComparator;
            } else {
                combinedComparator = combinedComparator.thenComparing(currentComparator);
            }
        }

        return combinedComparator != null ? combinedComparator : (o1, o2) -> 0;
    }

    private Comparator<Ticket> createComparatorForCriteria(SortCriteria criteria, SortType sortType) {
        Comparator<Ticket> comparator = null;

        switch (criteria) {
            case PRICE -> comparator = compareByPrice;
            case ARRIVAL_TIME -> comparator = compareByArrivalTime;
            case DEPARTURE_TIME -> comparator = compareByDepartureTime;
            case TRAVEL_TIME -> comparator = compareByTravelTime;
            default -> throw new UndefinedSortingCriteriaException("Sorting criteria are not supportable");
        }

        if (sortType == SortType.DESC) {
            comparator = comparator.reversed();
        }

        return comparator;
    }


}
