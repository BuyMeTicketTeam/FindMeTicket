package com.booking.app.services.impl;

import com.booking.app.dto.RequestTicketDTO;
import com.booking.app.dto.TicketDTO;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chrome.ChromeDriver;


@Service
@RequiredArgsConstructor
public class ScrapingServiceImpl {

    private static final String busforLink = "https://busfor.ua/автобуси/%s/%s?on=%s&passengers=1&search=true";
    private final ChromeDriver driver;

    public List<TicketDTO> scrapFromBusfor(RequestTicketDTO requestTicketDTO) {

        String url = String.format(busforLink, requestTicketDTO.getPlaceFrom(), requestTicketDTO.getPlaceAt(), requestTicketDTO.getDepartureDate());

        driver.get(url);

        try {
            synchronized (driver) {
                driver.wait(3000);
            }
        } catch (InterruptedException e) {
        }

        List<WebElement> products = driver.findElements(By.cssSelector("div.ticket"));

        List<TicketDTO> tickets = getTickets(url, products.size());

        return tickets;
    }

    private static String formatDate(String inputDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM u", new Locale("uk"));
        LocalDate date = LocalDate.parse(inputDate + " 2023", formatter);
        formatter = DateTimeFormatter.ofPattern("dd.MM, EEE", new Locale("uk"));
        return date.format(formatter);
    }


    private List<TicketDTO> getTickets(String busforUrl, int n) {

        List<TicketDTO> tickets = new LinkedList<>();

        for (int i = 0; i < n; i++) {

            driver.get(busforUrl);

            try {
                synchronized (driver) {
                    driver.wait(3000);
                }
            } catch (InterruptedException e) {

            }

            WebElement ticket = driver.findElements(By.cssSelector("div.ticket")).get(i);

            List<WebElement> ticketInfo = ticket.findElements(By.cssSelector("div.Style__Item-yh63zd-7.kAreny"));

            WebElement departureInfo = ticketInfo.get(0);
            WebElement arrivalInfo = ticketInfo.get(1);

            String departureDateTime = departureInfo.findElement(By.cssSelector("div.Style__Time-sc-1n9rkhj-0.bmnWRj")).getText();
            String arrivalDateTime = departureInfo.findElement(By.cssSelector("div.Style__Time-sc-1n9rkhj-0.bmnWRj")).getText();
            String travelTime = ticket.findElement(By.cssSelector("span.Style__TimeInRoad-yh63zd-0.btMUVs")).getText();

            TicketDTO temp = TicketDTO.builder()
                    .departureCity(departureInfo.findElement(By.cssSelector("div.Style__Title-yh63zd-5.cspGxb")).getText())
                    .arrivalCity(arrivalInfo.findElement(By.cssSelector("div.Style__Title-yh63zd-5.cspGxb")).getText())
                    .placeFrom(departureInfo.findElement(By.cssSelector("div.LinesEllipsis")).getText())
                    .placeAt(departureInfo.findElement(By.cssSelector("div.LinesEllipsis")).getText())
                    .departureTime(departureDateTime.substring(0, 5))
                    .departureDate(formatDate(departureDateTime.substring(6)))
                    .arrivalTime(arrivalDateTime.substring(0, 5))
                    .arrivalDate(formatDate(arrivalDateTime.substring(6)))
                    .price(ticket.findElement(By.cssSelector("span.price")).getText() + " грн")
                    .travelTime(travelTime.substring(0, travelTime.length() - 9)).build();

            String url = "";

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.elementToBeClickable(ticket.findElement(By.tagName("button")).findElement(By.tagName("span"))));

            ticket.findElement(By.tagName("button")).findElement(By.tagName("span")).click();

            wait.until(ExpectedConditions.urlContains("preorders"));

            url = driver.getCurrentUrl();


            temp.setUrl(url);

            tickets.add(temp);
        }

        return tickets;
    }

}
