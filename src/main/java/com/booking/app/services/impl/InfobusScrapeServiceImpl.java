package com.booking.app.services.impl;

import com.booking.app.dto.RequestTicketsDTO;
import com.booking.app.dto.TicketDTO;
import com.booking.app.entity.Route;
import com.booking.app.entity.Ticket;
import com.booking.app.entity.TicketUrls;
import com.booking.app.mapper.TicketMapper;
import com.booking.app.repositories.RouteRepository;
import com.booking.app.repositories.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Log4j2
public class InfobusScrapeServiceImpl {

    private static final String infobusLink = "https://infobus.eu/ua";
    private final RouteRepository routeRepository;
    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;


    @Async
    public CompletableFuture<Boolean> scrapeTickets(RequestTicketsDTO requestTicketDTO, SseEmitter emitter, Route route) throws ParseException, IOException {


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


        for (WebElement element : elements) {

            Ticket ticket = scrapeTicketInfo(element, route);


            synchronized (route) {
                if (route.getTickets().add(ticket)) {
                    emitter.send(SseEmitter.event().name("ticket data: ").data(ticketMapper.toDto(ticket)));
                }
            }
        }

        driver.quit();
        return CompletableFuture.completedFuture(true);
    }

    private Ticket scrapeTicketInfo(WebElement webTicket, Route route) throws ParseException {

        SimpleDateFormat ticketDate = new SimpleDateFormat("dd.MM yyyy");
        SimpleDateFormat formatedTicketDate = new SimpleDateFormat("dd.MM, E", new Locale("uk"));

        String arrivalDate = webTicket.findElements(By.cssSelector("span.day-preffix")).get(1).getText().substring(3) +" 2024";

        Date date = ticketDate.parse(arrivalDate);



        String price = webTicket.findElement(By.cssSelector("span.price-number")).getText().replace(" UAH", "");
        String travelTime = webTicket.findElement(By.className("duration-time")).getText().toLowerCase().replace(" г", "год.").replace(" хв", "хв");


        String[] parts = travelTime.split("[^\\d]+");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        int totalMinutes = hours * 60 + minutes;

        Ticket ticket = Ticket.builder()
                .id(UUID.randomUUID())
                .route(route)
                .price(BigDecimal.valueOf(Long.parseLong(price)))
                .placeFrom(webTicket.findElement(By.cssSelector("div.departure")).findElement(By.cssSelector("a.text-g")).getText())
                .placeAt(webTicket.findElement(By.cssSelector("div.arrival")).findElement(By.cssSelector("a.text-g")).getText())
                .travelTime(BigDecimal.valueOf(totalMinutes))
                .departureTime(webTicket.findElement(By.cssSelector("div.departure")).findElement(By.cssSelector("div.day_time")).findElements(By.tagName("span")).get(2).getText())
                .arrivalTime(webTicket.findElement(By.cssSelector("div.arrival")).findElement(By.cssSelector("div.day_time")).findElements(By.tagName("span")).get(2).getText())
                .arrivalDate(formatedTicketDate.format(date)).build();


        return ticket;
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
                ticket.getUrls().setInfobus(driver.getCurrentUrl());
                break;
            }
        }

        if (ticket.getUrls().getInfobus() != null) {
            synchronized (emitter) {
                emitter.send(SseEmitter.event().name("Infobus url:").data(ticket.getUrls().getInfobus()));
            }
        } else {
            synchronized (emitter) {
                emitter.send(SseEmitter.event().name("Infobus url:").data("no such url"));
            }
        }

        driver.quit();
        return CompletableFuture.completedFuture(true);
    }


    private void requestTickets(String departureCity, String arrivalCity, String departureDate, ChromeDriver driver) throws ParseException {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get(infobusLink);


        selectCity("city_from", "ui-id-1", departureCity, driver, wait);
        selectCity("city_to", "ui-id-2", arrivalCity, driver, wait);

        selectDate(departureDate, driver, wait);

        driver.findElement(By.id("main_form_search_button")).click();
    }

    private void selectCity(String inputCityId, String clickableElementId, String city, WebDriver driver, WebDriverWait wait) {
        WebElement inputCity = driver.findElement(By.id(inputCityId));
        inputCity.click();
        inputCity.sendKeys(city);
        wait.until(ExpectedConditions.elementToBeClickable(By.id(clickableElementId))).findElement(By.cssSelector("li.parent.bus.ui-menu-item")).click();
    }

    private void selectDate(String departureDate, WebDriver driver, WebDriverWait wait) throws ParseException {
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
    }
}
