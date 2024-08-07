package com.booking.app.constants;

import lombok.Getter;

import java.util.Random;


@Getter
public enum Website {
    PROIZD, BUSFOR, INFOBUS;

    private static final Random RANDOM = new Random();

    public static Website getRandom() {
        Website[] values = Website.values();
        return values[RANDOM.nextInt(values.length)];
    }

}
