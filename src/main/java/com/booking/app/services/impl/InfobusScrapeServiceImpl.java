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
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Log4j2
public class InfobusScrapeServiceImpl {

    private static final String infobusLink = "https://infobus.eu/ua";
    //private final ChromeDriver driver;
    private final RouteRepository routeRepository;
    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;


    @Async
    public CompletableFuture<Boolean> scrapeTickets(RequestTicketsDTO requestTicketDTO, SseEmitter emitter, Route route) throws ParseException, IOException {

        routeRepository.save(route);

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--headless");
        ChromeDriver driver = new ChromeDriver(options);

        requestTickets(requestTicketDTO.getDepartureCity(), requestTicketDTO.getArrivalCity(), requestTicketDTO.getDepartureDate(), driver);

        try {
            synchronized (driver) {
                driver.wait(5000);
            }
        } catch (InterruptedException e) {
        }

        List<WebElement> elements = driver.findElements(By.cssSelector("div.main-detail-wrap"));

        SimpleDateFormat ticketDate = new SimpleDateFormat("dd.MM");
        SimpleDateFormat formatedTicketDate = new SimpleDateFormat("dd.MM E", new Locale("uk"));

        for (WebElement element : elements) {

            String arrivalDate = element.findElements(By.cssSelector("span.day-preffix")).get(1).getText().substring(3);

            Date date = ticketDate.parse(arrivalDate);

            String price = element.findElement(By.cssSelector("span.price-number")).getText().replace(" UAH", "");
            String travelTime = element.findElement(By.className("duration-time")).getText().toLowerCase().replace(" г", "год.").replace(" хв", "хв");


            String[] parts = travelTime.split("[^\\d]+");
            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);
            int totalMinutes = hours * 60 + minutes;

            Ticket ticket = Ticket.builder()
                    .route(route)
                    .price(BigDecimal.valueOf(Long.parseLong(price)))
                    .placeFrom(element.findElement(By.cssSelector("div.departure")).findElement(By.cssSelector("a.text-g")).getText())
                    .placeAt(element.findElement(By.cssSelector("div.arrival")).findElement(By.cssSelector("a.text-g")).getText())
                    .travelTime(BigDecimal.valueOf(totalMinutes))
                    .departureTime(element.findElement(By.cssSelector("div.departure")).findElement(By.cssSelector("div.day_time")).findElements(By.tagName("span")).get(2).getText())
                    .arrivalTime(element.findElement(By.cssSelector("div.arrival")).findElement(By.cssSelector("div.day_time")).findElements(By.tagName("span")).get(2).getText())
                    .arrivalDate(formatedTicketDate.format(date)).build();


            synchronized (emitter) {
                ticket = ticketRepository.save(ticket);
                emitter.send(SseEmitter.event().name("ticket data: ").data(ticketMapper.toDto(ticket)));
            }

        }

        driver.quit();
        return CompletableFuture.completedFuture(true);
    }

    @Async
    public CompletableFuture<Boolean> getTicket(SseEmitter emitter, Ticket ticket) throws IOException, ParseException {

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--headless");
        ChromeDriver driver = new ChromeDriver(options);

        Route route = ticket.getRoute();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        requestTickets(route.getDepartureCity(), route.getArrivalCity(), route.getDepartureDate(), driver);

        try {
            synchronized (driver) {
                driver.wait(5000);
            }
        } catch (InterruptedException e) {
        }

        List<WebElement> elements = driver.findElements(By.cssSelector("div.trip"));

        for (WebElement element : elements) {

            String priceString = element.findElement(By.cssSelector("span.price-number")).getText().replace(" UAH", "");
            priceString = priceString.substring(0, priceString.length() - 6);

            String departureTime = element.findElement(By.cssSelector("div.departure")).findElement(By.cssSelector("div.day_time")).findElements(By.tagName("span")).get(2).getText();

            String arrivalTime = element.findElement(By.cssSelector("div.arrival")).findElement(By.cssSelector("div.day_time")).findElements(By.tagName("span")).get(2).getText();

            if (ticket.getDepartureTime().equals(departureTime) &&
                    ticket.getArrivalTime().equals(arrivalTime) &&
                    ticket.getPrice().equals(BigDecimal.valueOf(Long.parseLong(priceString)))) {

                element.findElement(By.cssSelector("button.btn")).click();
                wait.until(ExpectedConditions.urlContains("deeplink"));
                ticket.setUrl(driver.getCurrentUrl());
                break;
            }
        }

        if (ticket.getUrl() != null) {
            synchronized (emitter) {
                ticketRepository.save(ticket);
                emitter.send(SseEmitter.event().name("Infobus url:").data(ticket.getUrl()));
            }
        } else {
            synchronized (emitter) {
                emitter.send(SseEmitter.event().name("Infobus url:").data("no such url"));
            }
        }
//        }else {
//            synchronized (emitter) {
//                ticketRepository.save(ticket);
//                emitter.send(SseEmitter.event().name("Infobus url:").data(ticket.getUrls().getProizd()));
//            }
//        }

        driver.quit();
        return CompletableFuture.completedFuture(true);
    }


    private void requestTickets(String departureCity, String arrivalCity, String departureDate, ChromeDriver driver) throws ParseException {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get(infobusLink);

        WebElement cityFrom = driver.findElement(By.id("city_from"));
        cityFrom.click();
        cityFrom.sendKeys(departureCity);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("ui-id-1"))).findElement(By.cssSelector("li.parent.bus.ui-menu-item")).click();


        WebElement cityTo = driver.findElement(By.id("city_to"));
        wait.until(ExpectedConditions.elementToBeClickable(cityTo)).click();
        cityTo.sendKeys(arrivalCity);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("ui-id-2"))).findElement(By.cssSelector("li.parent.bus.ui-menu-item")).click();


        WebElement dateFrom = driver.findElement(By.id("dateFrom"));
        dateFrom.click();

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outputMonthFormat = new SimpleDateFormat("MMMM", new Locale("uk"));
        SimpleDateFormat outputYearFormat = new SimpleDateFormat("yyyy", new Locale("uk"));
        SimpleDateFormat outputDayFormat = new SimpleDateFormat("d", new Locale("uk"));

        Date inputDate = inputFormat.parse(departureDate);

        String requestMonth = outputMonthFormat.format(inputDate);
        String requestYear = outputYearFormat.format(inputDate);
        String requestDay = outputDayFormat.format(inputDate);

        String calendarMonth = driver.findElement(By.cssSelector("span.ui-datepicker-month")).getText();
        String calendarYear = driver.findElement(By.cssSelector("span.ui-datepicker-year")).getText();

        while (!(calendarMonth.toLowerCase().equals(requestMonth) && calendarYear.equals(requestYear))) {

            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@data-handler=\"next\"]"))).click();

            calendarMonth = driver.findElement(By.cssSelector("span.ui-datepicker-month")).getText();
            calendarYear = driver.findElement(By.cssSelector("span.ui-datepicker-year")).getText();
        }

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a.ui-state-default")));

        driver.findElements(By.cssSelector("a.ui-state-default")).stream().filter(element -> element.getText().equals(requestDay)).findFirst().get().click();

        driver.findElement(By.id("main_form_search_button")).click();
    }

}
