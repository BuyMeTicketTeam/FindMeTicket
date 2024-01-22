package com.booking.app.services.impl;

import com.booking.app.dto.RequestTicketsDTO;
import com.booking.app.dto.TicketDTO;
import com.booking.app.entity.Route;
import com.booking.app.entity.Ticket;
import com.booking.app.mapper.TicketMapper;
import com.booking.app.repositories.RouteRepository;
import com.booking.app.repositories.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BusforScrapeServiceImpl {

    private static final String busforLink = "https://busfor.ua/автобуси/%s/%s?on=%s&passengers=1&search=true";
    //private final ChromeDriver driver;
    private final RouteRepository routeRepository;
    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;

    public void scrapeTickets(RequestTicketsDTO requestTicketDTO, SseEmitter emitter, Route route) throws IOException {

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--headless");
        ChromeDriver driver = new ChromeDriver(options);

        String url = String.format(busforLink, requestTicketDTO.getDepartureCity(), requestTicketDTO.getArrivalCity(), requestTicketDTO.getDepartureDate());

        driver.get(url);

        try {
            synchronized (driver) {
                driver.wait(5000);
            }
        } catch (InterruptedException e) {
        }

        List<WebElement> products = driver.findElements(By.cssSelector("div.ticket"));


        for (int i = 0; i < products.size() && i < 150; i++) {

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
                    .route(route)
                    .build();

            synchronized (route) {
                route.getTicketList().add(temp);
            }

            synchronized (emitter) {
                emitter.send(SseEmitter.event().name("ticket data: ").data(toDTO(temp, route)));
            }
        }


        driver.quit();
    }

    @Async
    public void getTicket(UUID id, SseEmitter emitter) throws IOException {

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--headless");
        ChromeDriver driver = new ChromeDriver(options);

        Ticket ticket = ticketRepository.findById(id).orElseThrow();

        if (ticket.getUrl() == null || ticket.getUrl().isEmpty() || ticket.getUrl().equals("null")) {

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
                        ticket.getDepartureTime().equals(departureDateTime.substring(0, 5)) &&
                        ticket.getTravelTime().equals(travelTime.substring(0, travelTime.length() - 9))) {

                    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
                    element.findElement(By.tagName("button")).findElement(By.tagName("span")).click();
                    wait.until(ExpectedConditions.urlContains("preorders"));
                    url = driver.getCurrentUrl();
                    ticket.setUrl(url);
                }
            }

            if (ticket.getUrl() != null) {
                synchronized (emitter) {
                    emitter.send(SseEmitter.event().name("Busfor url:").data(ticket.getUrl()));
                }
            } else
                synchronized (emitter) {
                    emitter.send(SseEmitter.event().name("Busfor url:").data("no such url"));
                }
        }

        driver.quit();
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

}
