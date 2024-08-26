package com.booking.app.constants;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
public enum TransportType {
    BUS,
    TRAIN,
    AIRPLANE,
    FERRY;

    public static TransportType[] getTypes(boolean bus, boolean train, boolean airplane, boolean ferry) {
        Set<TransportType> types = new HashSet<>();
        if (bus) types.add(TransportType.BUS);
        if (train) types.add(TransportType.TRAIN);
        if (airplane) types.add(TransportType.AIRPLANE);
        if (ferry) types.add(TransportType.FERRY);
        return types.toArray(new TransportType[0]);
    }

}
