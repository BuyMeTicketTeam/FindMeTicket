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
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class ProizdScrapeServiceImpl {

    private static final String proizdLink = "https://bus.proizd.ua/";
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

        List<WebElement> elements = driver.findElements(By.cssSelector("div.trip"));

        SimpleDateFormat ticketDate = new SimpleDateFormat("d MMMM", new Locale("uk"));
        SimpleDateFormat formatedTicketDate = new SimpleDateFormat("dd.MM E", new Locale("uk"));

        for (WebElement element : elements) {

            String arrivalDate = element.findElements(By.cssSelector("div.trip__date")).get(1).getText();
            arrivalDate = arrivalDate.substring(5);

            Date date = ticketDate.parse(arrivalDate);

            String price = element.findElement(By.cssSelector("div.carriage-bus__price")).getText();
            price = price.substring(0, price.length() - 6);

            String travelTime = element.findElement(By.cssSelector("div.travel-time__value")).getText();
            travelTime = travelTime.replaceFirst("г", "год.");


            String[] parts = travelTime.split("[^\\d]+");
            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);
            int totalMinutes = hours * 60 + minutes;


            Ticket ticket = Ticket.builder()
                    .route(route)
                    .price(BigDecimal.valueOf(Long.parseLong(price)))
                    .placeFrom(element.findElements(By.cssSelector("div.trip__station-address")).get(0).getText())
                    .placeAt(element.findElements(By.cssSelector("div.trip__station-address")).get(1).getText())
                    .travelTime(BigDecimal.valueOf(totalMinutes))
                    .departureTime(element.findElements(By.cssSelector("div.trip__time")).get(0).getText())
                    .arrivalTime(element.findElements(By.cssSelector("div.trip__time")).get(1).getText())
                    .arrivalDate(formatedTicketDate.format(date).replace(" ", ", ")).build();


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


        requestTickets(route.getDepartureCity(), route.getArrivalCity(), route.getDepartureDate(), driver);

        try {
            synchronized (driver) {
                driver.wait(5000);
            }
        } catch (InterruptedException e) {
        }

        List<WebElement> elements = driver.findElements(By.cssSelector("div.trip"));

        for (WebElement element : elements) {

            String priceString = element.findElement(By.cssSelector("div.carriage-bus__price")).getText();
            priceString = priceString.substring(0, priceString.length() - 6);

            String departureTime = element.findElements(By.cssSelector("div.trip__time")).get(0).getText();
            String arrivalTime = element.findElements(By.cssSelector("div.trip__time")).get(1).getText();

            if (ticket.getDepartureTime().equals(departureTime) &&
                    ticket.getArrivalTime().equals(arrivalTime) &&
                    ticket.getPrice().equals(BigDecimal.valueOf(Long.parseLong(priceString)))) {
                ticket.setUrl(element.findElement(By.cssSelector("a.btn")).getAttribute("href"));
                break;
            }
        }

        if (ticket.getUrl() != null) {
            synchronized (emitter) {
                ticketRepository.save(ticket);
                emitter.send(SseEmitter.event().name("Proizd url:").data(ticket.getUrl()));
            }
        } else {
            synchronized (emitter) {
                emitter.send(SseEmitter.event().name("Proizd url:").data("no such url"));
            }
        }

        driver.quit();
        return CompletableFuture.completedFuture(true);
    }

    private void requestTickets(String departureCity, String arrivalCity, String departureDate, ChromeDriver driver) throws ParseException {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get(proizdLink);


        WebElement cityFrom = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@placeholder='Станція відправлення']")));
        cityFrom.click();
        cityFrom.sendKeys(departureCity);
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("li.station-item.active.ng-star-inserted"))).click();


        WebElement cityTo = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@placeholder='Станція прибуття']")));
        wait.until(ExpectedConditions.elementToBeClickable(cityTo)).click();
        cityTo.sendKeys(arrivalCity);
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("li.station-item.active.ng-star-inserted"))).click();


        while (true) {
            WebElement dateFrom = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div.search-form__field.search-form__date")));
            dateFrom.click();

            try {
                synchronized (driver) {
                    driver.wait(100);
                }
            } catch (InterruptedException e) {
            }

            String calendarMonth = driver.findElement(By.cssSelector("li.calmonth")).getText();

            if (calendarMonth.length() != 0) break;
        }

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outputMonthFormat = new SimpleDateFormat("MMMM", new Locale("uk"));
        SimpleDateFormat outputYearFormat = new SimpleDateFormat("yyyy", new Locale("uk"));
        SimpleDateFormat outputDayFormat = new SimpleDateFormat("d", new Locale("uk"));

        Date inputDate = inputFormat.parse(departureDate);

        String requestMonth = outputMonthFormat.format(inputDate);
        String requestYear = outputYearFormat.format(inputDate);
        String requestDay = outputDayFormat.format(inputDate);

        String calendarMonth = driver.findElement(By.cssSelector("li.calmonth")).getText();
        calendarMonth = calendarMonth.substring(0, calendarMonth.length() - 5);

        String calendarYear = driver.findElement(By.cssSelector("li.calmonth")).getText();
        calendarYear = calendarYear.substring(calendarYear.length() - 4);

        while (!(calendarMonth.toLowerCase().equals(requestMonth) && calendarYear.equals(requestYear))) {

            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("li.calnextmonth"))).click();

            calendarMonth = driver.findElement(By.cssSelector("li.calmonth")).getText();
            calendarMonth = calendarMonth.substring(0, calendarMonth.length() - 5);

            calendarYear = driver.findElement(By.cssSelector("li.calmonth")).getText();
            calendarYear = calendarYear.substring(calendarYear.length() - 4);
        }

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div.calbody")));

        driver.findElement(By.cssSelector("div.calbody")).findElements(By.tagName("li")).stream().filter(element -> element.getText().equals(requestDay)).findFirst().get().click();

        driver.findElement(By.cssSelector("button.btn.search-form__btn")).click();
    }

}
