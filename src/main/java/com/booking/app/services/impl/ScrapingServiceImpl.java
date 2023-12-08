package com.booking.app.services.impl;

import com.booking.app.dto.RequestTicketDTO;
import com.booking.app.dto.TicketDTO;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chrome.ChromeDriver;


@Service
@AllArgsConstructor
public class ScrapingServiceImpl {

    private static final String busforLink = "https://busfor.ua/автобуси/%s/%s?on=%s&passengers=1&search=true";
    private ChromeDriver driver;

    public List<TicketDTO> scrapFromBusfor(RequestTicketDTO requestTicketDTO) {
        driver.get(String.format(busforLink, requestTicketDTO.getPlaceFrom(), requestTicketDTO.getPlaceAt(), requestTicketDTO.getDepartureDate()));

        try {
            synchronized (driver) {
                driver.wait(1000);
            }
        } catch (InterruptedException e) {
        }

        List<TicketDTO> tickets = new LinkedList<>();

        List<WebElement> products = driver.findElements(By.cssSelector("div.ticket"));

        System.out.println(products.size());

        for (WebElement element : products) {

            List<WebElement> ticketInfo = element.findElements(By.cssSelector("div.Style__Item-yh63zd-7.kAreny"));

            WebElement departureInfo = ticketInfo.get(0);

            WebElement arrivalInfo = ticketInfo.get(1);

            String departureDateTime = departureInfo.findElement(By.cssSelector("div.Style__Time-sc-1n9rkhj-0.bmnWRj")).getText();
            String arrivalDateTime = departureInfo.findElement(By.cssSelector("div.Style__Time-sc-1n9rkhj-0.bmnWRj")).getText();
            String travelTime = element.findElement(By.cssSelector("span.Style__TimeInRoad-yh63zd-0.btMUVs")).getText();

            TicketDTO temp = TicketDTO.builder()
                    .departureCity(departureInfo.findElement(By.cssSelector("div.Style__Title-yh63zd-5.cspGxb")).getText())
                    .arrivalCity(arrivalInfo.findElement(By.cssSelector("div.Style__Title-yh63zd-5.cspGxb")).getText())
                    .placeFrom(departureInfo.findElement(By.cssSelector("div.LinesEllipsis")).getText())
                    .placeAt(departureInfo.findElement(By.cssSelector("div.LinesEllipsis")).getText())
                    .departureTime(departureDateTime.substring(0, 5))
                    .departureDate(formatDate(departureDateTime.substring(6)))
                    .arrivalTime(arrivalDateTime.substring(0, 5))
                    .arrivalDate(formatDate(arrivalDateTime.substring(6)))
                    .price(element.findElement(By.cssSelector("span.price")).getText() + " грн")
                    .travelTime(travelTime.substring(0, travelTime.length() - 9)).build();

            tickets.add(temp);
        }

        driver.quit();

        return tickets;
    }

    private static String formatDate(String inputDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM u", new Locale("uk"));
        LocalDate date = LocalDate.parse(inputDate + " 2023", formatter);
        formatter = DateTimeFormatter.ofPattern("dd.MM, EEE", new Locale("uk"));
        return date.format(formatter);
    }


}
