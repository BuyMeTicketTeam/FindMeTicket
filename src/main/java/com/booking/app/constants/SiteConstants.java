package com.booking.app.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SiteConstants {
    public static final String PROIZD_UA = "proizd.ua";
    public static final String BUSFOR_UA = "busfor.ua";
    public static final String INFOBUS = "infobus.eu";


    private static final List<String> SITES = Arrays.asList(PROIZD_UA, BUSFOR_UA, INFOBUS);
    private static final Random RANDOM = new Random();

    public static String getRandomSite() {
        return SITES.get(RANDOM.nextInt(SITES.size()));
    }

}
