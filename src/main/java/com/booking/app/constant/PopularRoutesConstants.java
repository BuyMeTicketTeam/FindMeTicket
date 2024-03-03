package com.booking.app.constant;

import com.booking.app.util.City;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PopularRoutesConstants {
    private static final List<City> popularRoutes = new ArrayList<>();

    static {
        popularRoutes.add(new City(703448L, 706483L)); // Kyiv-Kharkiv
        popularRoutes.add(new City(709930L, 710719L)); // Dnipro-Chernivtsi
        popularRoutes.add(new City(703448L, 709930L));// Kyiv-Dnipro
        popularRoutes.add(new City(706483L, 707471L));// Kharkiv-Ivano-Frankivsk
        popularRoutes.add(new City(702550L, 703448L));// Lviv-Kyiv
        popularRoutes.add(new City(709930L, 698740L));// Dnipro-Odesa
        popularRoutes.add(new City(703448L, 696643L));// Kyiv-Poltava
        popularRoutes.add(new City(702550L, 691650L));// Lviv-Ternopil
        popularRoutes.add(new City(706483L, 687700L));// Kharkiv-Zaporizhzhia
        popularRoutes.add(new City(698740L, 706448L));// Odesa-Kherson
        popularRoutes.add(new City(687700L, 703845L));// Zaporizhzhia-Kryvyi Rih
    }

    public static List<City> getPopularRoutes() {
        return List.copyOf(popularRoutes);
    }

}
