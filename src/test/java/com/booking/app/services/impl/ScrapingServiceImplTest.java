package com.booking.app.services.impl;

import com.booking.app.dto.RequestTicketDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ScrapingServiceImplTest {

    @InjectMocks
    ScrapingServiceImpl scrapingService;

//    @Test
//    void scrapFromBusfor() {
//        System.out.println(scrapingService
//                .scrapFromBusfor(RequestTicketDTO.builder().placeFrom("Київ").placeAt("Харків").departureDate("2023-12-10").build()));
//    }
}