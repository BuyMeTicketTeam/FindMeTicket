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
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProizdScrapeServiceImpl {

    private static final String proizdLink = "https://bus.proizd.ua/";
    private final RouteRepository routeRepository;
    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;

    public void scrapeTickets(RequestTicketsDTO requestTicketDTO, SseEmitter emitter, Route route) throws ParseException, IOException {

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--headless");
        ChromeDriver driver = new ChromeDriver(options);

        requestTickets(requestTicketDTO, driver);

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
            price = price.substring(0, price.length() - 3);

            String travelTime = element.findElement(By.cssSelector("div.travel-time__value")).getText();
            travelTime = travelTime.replaceFirst("г", "год.");


            Ticket ticket = Ticket.builder()
                    .price(price)
                    .placeFrom(element.findElements(By.cssSelector("div.trip__station-address")).get(0).getText())
                    .placeAt(element.findElements(By.cssSelector("div.trip__station-address")).get(1).getText())
                    .travelTime(travelTime)
                    .departureTime(element.findElements(By.cssSelector("div.trip__time")).get(0).getText())
                    .arrivalTime(element.findElements(By.cssSelector("div.trip__time")).get(1).getText())
                    .arrivalDate(formatedTicketDate.format(date)).build();

            synchronized (route) {
                route.getTicketList().add(ticket);
            }

            synchronized (emitter) {
                emitter.send(SseEmitter.event().name("ticket data: ").data(toDTO(ticket, route)));
            }
        }


        driver.quit();
    }

    private TicketDTO toDTO(Ticket ticket, Route route) {
        TicketDTO result = ticketMapper.toTicketDTO(ticket);
        result.setDepartureCity(route.getDepartureCity());
        result.setArrivalCity(route.getArrivalCity());
        result.setDepartureDate(route.getDepartureDate());
        return result;
    }


    private void requestTickets(RequestTicketsDTO requestTicketsDTO, ChromeDriver driver) throws ParseException {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get(proizdLink);


        WebElement cityFrom = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@placeholder='Станція відправлення']")));
        cityFrom.click();
        cityFrom.sendKeys(requestTicketsDTO.getDepartureCity());
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("li.station-item.active.ng-star-inserted"))).click();


        WebElement cityTo = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@placeholder='Станція прибуття']")));
        wait.until(ExpectedConditions.elementToBeClickable(cityTo)).click();
        cityTo.sendKeys(requestTicketsDTO.getArrivalCity());
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

        Date inputDate = inputFormat.parse(requestTicketsDTO.getDepartureDate());

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
