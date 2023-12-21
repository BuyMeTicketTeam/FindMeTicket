package com.booking.app.services.impl;

import com.booking.app.dto.RequestTicketsDTO;
import com.booking.app.dto.TicketDTO;

import com.booking.app.entity.Route;
import com.booking.app.entity.Ticket;
import com.booking.app.mapper.TicketMapper;
import com.booking.app.repositories.RouteRepository;
import com.booking.app.repositories.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


@Service
@RequiredArgsConstructor
@Log4j2
public class ScrapingServiceImpl {

    private static final String busforLink = "https://busfor.ua/автобуси/%s/%s?on=%s&passengers=1&search=true";
    private final ChromeDriver driver;
    private final RouteRepository routeRepository;
    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;

    @Async
    public void getTicket(UUID id, SseEmitter emitter) throws IOException {

        Ticket ticket = ticketRepository.findById(id).orElseThrow();

        emitter.send(SseEmitter.event().name("sad ").data("sad ticket " + toDTO(ticket, ticket.getRoute())));


        if (ticket.getUrl() == null ||ticket.getUrl().isEmpty() || ticket.getUrl().equals("null")) {
            String departureCity = ticket.getRoute().getDepartureCity();
            String arrivalCity = ticket.getRoute().getArrivalCity();
            String departureDate = ticket.getRoute().getDepartureDate();

            String url = String.format(busforLink, departureCity, arrivalCity, departureDate);

            driver.get(url);

            try {
                synchronized (driver) {
                    driver.wait(5000);
                }
            } catch (InterruptedException e) {
            }

            List<WebElement> tickets = driver.findElements(By.cssSelector("div.ticket"));

            for (WebElement element : tickets) {

                List<WebElement> ticketInfo = element.findElements(By.cssSelector("div.Style__Item-yh63zd-7.kAreny"));

                WebElement departureInfo = ticketInfo.get(0);
                WebElement arrivalInfo = ticketInfo.get(1);

                String departureDateTime = departureInfo.findElement(By.cssSelector("div.Style__Time-sc-1n9rkhj-0.bmnWRj")).getText();
                String arrivalDateTime = arrivalInfo.findElement(By.cssSelector("div.Style__Time-sc-1n9rkhj-0.bmnWRj")).getText();
                String travelTime = element.findElement(By.cssSelector("span.Style__TimeInRoad-yh63zd-0.btMUVs")).getText();

                if (ticket.getArrivalDate().equals(formatDate(arrivalDateTime.substring(6))) &&
                        ticket.getPrice().equals(element.findElement(By.cssSelector("span.price")).getText()) &&
                        ticket.getArrivalTime().equals(arrivalDateTime.substring(0, 5)) &&
                        ticket.getPlaceAt().equals(arrivalInfo.findElement(By.cssSelector("div.LinesEllipsis")).getText()) &&
                        ticket.getDepartureTime().equals(departureDateTime.substring(0, 5)) &&
                        ticket.getTravelTime().equals(travelTime.substring(0, travelTime.length() - 9)) &&
                        ticket.getPlaceFrom().equals(departureInfo.findElement(By.cssSelector("div.LinesEllipsis")).getText())) {

                    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                    element.findElement(By.tagName("button")).findElement(By.tagName("span")).click();
                    wait.until(ExpectedConditions.urlContains("preorders"));
                    url = driver.getCurrentUrl();
                    ticket.setUrl(url);
                    //ticketRepository.changeUrlById(id, url);
                }

            }
            emitter.send(SseEmitter.event().name("ticket Url ").data("ticket url = " + ticket.getUrl()));

            emitter.complete();
        }


    }

    @Async
    public void scrapFromBusfor(RequestTicketsDTO requestTicketDTO, SseEmitter emitter) throws IOException {

        Route routeFromDb = routeRepository.findByDepartureCityAndArrivalCityAndDepartureDate(requestTicketDTO.getDepartureCity(), requestTicketDTO.getArrivalCity(), requestTicketDTO.getDepartureDate());

        if (routeFromDb == null) {

            String url = String.format(busforLink, requestTicketDTO.getDepartureCity(), requestTicketDTO.getArrivalCity(), requestTicketDTO.getDepartureDate());

            Route route = Route.builder()
                    .id(UUID.randomUUID())
                    .departureCity(requestTicketDTO.getDepartureCity())
                    .arrivalCity(requestTicketDTO.getArrivalCity())
                    .departureDate(requestTicketDTO.getDepartureDate())
                    .addingTime(LocalDateTime.now())
                    .ticketList(new ArrayList<>()).build();

            driver.get(url);

            try {
                synchronized (driver) {
                    driver.wait(5000);
                }
            } catch (InterruptedException e) {
            }

            List<WebElement> products = driver.findElements(By.cssSelector("div.ticket"));

            log.info(products.size());

            for (int i = 0; i < products.size(); i++) {

                WebElement ticket = driver.findElements(By.cssSelector("div.ticket")).get(i);

                List<WebElement> ticketInfo = ticket.findElements(By.cssSelector("div.Style__Item-yh63zd-7.kAreny"));

                WebElement departureInfo = ticketInfo.get(0);
                WebElement arrivalInfo = ticketInfo.get(1);

                String departureDateTime = departureInfo.findElement(By.cssSelector("div.Style__Time-sc-1n9rkhj-0.bmnWRj")).getText();
                String arrivalDateTime = arrivalInfo.findElement(By.cssSelector("div.Style__Time-sc-1n9rkhj-0.bmnWRj")).getText();
                String travelTime = ticket.findElement(By.cssSelector("span.Style__TimeInRoad-yh63zd-0.btMUVs")).getText();

                Ticket temp = Ticket.builder()
                        .id(UUID.randomUUID())
                        .placeFrom(departureInfo.findElement(By.cssSelector("div.LinesEllipsis")).getText())
                        .placeAt(arrivalInfo.findElement(By.cssSelector("div.LinesEllipsis")).getText())
                        .departureTime(departureDateTime.substring(0, 5))
                        .arrivalTime(arrivalDateTime.substring(0, 5))
                        .arrivalDate(formatDate(arrivalDateTime.substring(6)))
                        .price(ticket.findElement(By.cssSelector("span.price")).getText())
                        .travelTime(travelTime.substring(0, travelTime.length() - 9))
                        .route(route).build();


                route.getTicketList().add(temp);


                emitter.send(SseEmitter.event().name("ticket data: ").data(toDTO(temp, route)));
            }
            routeRepository.save(route);
        } else {
            log.info("sad + " + routeFromDb.getTicketList().size());
            for (Ticket ticket : routeFromDb.getTicketList()) {
                emitter.send(SseEmitter.event().name("ticket data: ").data(toDTO(ticket, routeFromDb)));
            }
        }
        emitter.complete();
    }

    private static String formatDate(String inputDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM u", new Locale("uk"));
        LocalDate date = LocalDate.parse(inputDate + " 2023", formatter);
        formatter = DateTimeFormatter.ofPattern("dd.MM, EEE", new Locale("uk"));
        return date.format(formatter);
    }

    private TicketDTO toDTO(Ticket ticket, Route route) {
        TicketDTO result = ticketMapper.toTicketDTO(ticket);
        result.setDepartureCity(route.getDepartureCity());
        result.setArrivalCity(route.getArrivalCity());
        result.setDepartureDate(route.getDepartureDate());
        return result;
    }

    @Async
    @Scheduled(fixedRate = 1800000)
    public void deleteOldTickets() {
        LocalDateTime fifteenMinutesAgo = LocalDateTime.now().minusMinutes(15);

        routeRepository.deleteOlderThan(fifteenMinutesAgo);
    }

}
