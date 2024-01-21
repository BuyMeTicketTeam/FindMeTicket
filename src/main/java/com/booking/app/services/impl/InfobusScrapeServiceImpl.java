package com.booking.app.services.impl;

import com.booking.app.dto.RequestTicketsDTO;
import com.booking.app.dto.TicketDTO;
import com.booking.app.entity.Route;
import com.booking.app.entity.Ticket;
import com.booking.app.mapper.TicketMapper;
import com.booking.app.repositories.RouteRepository;
import com.booking.app.repositories.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
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
import java.util.*;

@Service
@RequiredArgsConstructor
public class InfobusScrapeServiceImpl {

    private static final String infobusLink = "https://infobus.eu/ua";
    //private final ChromeDriver driver;
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


        List<WebElement> elements = driver.findElements(By.cssSelector("div.main-detail-wrap"));

        SimpleDateFormat ticketDate = new SimpleDateFormat("dd.MM");
        SimpleDateFormat formatedTicketDate = new SimpleDateFormat("dd.MM E", new Locale("uk"));

        for (WebElement element : elements) {

            String arrivalDate = element.findElements(By.cssSelector("span.day-preffix")).get(1).getText().substring(3);

            Date date = ticketDate.parse(arrivalDate);

            Ticket ticket = Ticket.builder()
                    .id(UUID.randomUUID())
                    .price(element.findElement(By.cssSelector("span.price-number")).getText())
                    .placeFrom(element.findElement(By.cssSelector("div.departure")).findElement(By.cssSelector("a.text-g")).getText())
                    .placeAt(element.findElement(By.cssSelector("div.arrival")).findElement(By.cssSelector("a.text-g")).getText())
                    .travelTime(element.findElement(By.className("duration-time")).getText().toLowerCase())
                    .departureTime(element.findElement(By.cssSelector("div.departure")).findElement(By.cssSelector("div.day_time")).findElements(By.tagName("span")).get(1).getText())
                    .arrivalTime(element.findElement(By.cssSelector("div.arrival")).findElement(By.cssSelector("div.day_time")).findElements(By.tagName("span")).get(1).getText())
                    .arrivalDate(formatedTicketDate.format(date)).build();

            synchronized(route){
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

        driver.get(infobusLink);

        WebElement cityFrom = driver.findElement(By.id("city_from"));
        cityFrom.click();
        cityFrom.sendKeys(requestTicketsDTO.getDepartureCity());
        wait.until(ExpectedConditions.elementToBeClickable(By.id("ui-id-1"))).findElement(By.cssSelector("li.parent.bus.ui-menu-item")).click();


        WebElement cityTo = driver.findElement(By.id("city_to"));
        wait.until(ExpectedConditions.elementToBeClickable(cityTo)).click();
        cityTo.sendKeys(requestTicketsDTO.getArrivalCity());
        wait.until(ExpectedConditions.elementToBeClickable(By.id("ui-id-2"))).findElement(By.cssSelector("li.parent.bus.ui-menu-item")).click();


        WebElement dateFrom = driver.findElement(By.id("dateFrom"));
        dateFrom.click();

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outputMonthFormat = new SimpleDateFormat("MMMM", new Locale("uk"));
        SimpleDateFormat outputYearFormat = new SimpleDateFormat("yyyy", new Locale("uk"));
        SimpleDateFormat outputDayFormat = new SimpleDateFormat("d", new Locale("uk"));

        Date inputDate = inputFormat.parse(requestTicketsDTO.getDepartureDate());

        String requestMonth = outputMonthFormat.format(inputDate);
        String requestYear = outputYearFormat.format(inputDate);
        String requestDay = outputDayFormat.format(inputDate);

        System.out.println(requestDay + " " + requestMonth + " " + requestYear);

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
