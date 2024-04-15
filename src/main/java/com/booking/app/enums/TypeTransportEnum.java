package com.booking.app.enums;

import java.util.HashSet;
import java.util.Set;

public enum TypeTransportEnum {
    BUS,
    TRAIN,
    AIRPLANE,
    FERRY;

    public static Set<TypeTransportEnum> getTypes(boolean bus, boolean train, boolean airplane, boolean ferry) {
        Set<TypeTransportEnum> hashSet = new HashSet<>();
        if (bus) {
            hashSet.add(BUS);
        }
        if (train) {
            hashSet.add(TRAIN);
        }
        if (airplane) {
            hashSet.add(AIRPLANE);
        }
        if (ferry) {
            hashSet.add(FERRY);
        }
        return hashSet;
    }
}
