package com.booking.app.services.impl;

import com.booking.app.config.LinkProps;
import com.booking.app.constant.SiteConstants;
import com.booking.app.dto.RequestTicketsDTO;
import com.booking.app.entity.Route;
import com.booking.app.entity.Ticket;
import com.booking.app.enums.TypeTransportEnum;
import com.booking.app.mapper.TicketMapper;
import com.booking.app.services.ScraperService;
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
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service("infobus")
@RequiredArgsConstructor
@Log4j2
public class InfobusScraperServiceImpl implements ScraperService {

    private final LinkProps linkProps;

    private final TicketMapper ticketMapper;

    @Async
    public CompletableFuture<Boolean> scrapeTickets(RequestTicketsDTO requestTicketDTO, SseEmitter emitter, Route route) throws ParseException, IOException {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--headless");
        ChromeDriver driver = new ChromeDriver(options);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        requestTickets(requestTicketDTO.getDepartureCity(), requestTicketDTO.getArrivalCity(), requestTicketDTO.getDepartureDate(), driver);

        String DIV_TICKET = "div.main-detail-wrap";
        try {
            wait.until(ExpectedConditions.or(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.col-sm-12.alert.alert-warning")), ExpectedConditions.presenceOfElementLocated(By.cssSelector(DIV_TICKET))));
            driver.findElement(By.cssSelector(DIV_TICKET));
        } catch (Exception e) {
            driver.quit();
            log.info("INFOBUS TICKETS IN scrapeTickets(): NOT FOUND");
            return CompletableFuture.completedFuture(false);
        }

        try {
            synchronized (driver) {
                driver.wait(5000);
            }
        } catch (InterruptedException e) {
        }

        List<WebElement> elements = driver.findElements(By.cssSelector(DIV_TICKET));
        log.info("INFOBUS TICKETS IN scrapeTickets(): " + elements.size());

        for (int i = 0; i < elements.size() && i < 150; i++) {
            Ticket ticket = scrapeTicketInfo(elements.get(i), route);
            if (route.getTickets().add(ticket)) {
                emitter.send(SseEmitter.event().name("ticket data: ").data(ticketMapper.toDto(ticket)));
            }
        }

        driver.quit();
        return CompletableFuture.completedFuture(true);
    }

    private Ticket scrapeTicketInfo(WebElement webTicket, Route route) throws ParseException {
        SimpleDateFormat ticketDate = new SimpleDateFormat("dd.MM yyyy");
        SimpleDateFormat formatedTicketDate = new SimpleDateFormat("dd.MM, E", new Locale("uk"));

        String arrivalDate = webTicket.findElements(By.cssSelector("span.day-preffix")).get(1).getText().substring(3) + " 2024";

        Date date = ticketDate.parse(arrivalDate);


        String price = webTicket.findElement(By.cssSelector("span.price-number")).getText().replace(" UAH", "");
        String travelTime = webTicket.findElement(By.className("duration-time")).getText().toLowerCase().replace(" г", "год.").replace(" хв", "хв");


        String[] parts = travelTime.split("[^\\d]+");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        int totalMinutes = hours * 60 + minutes;

        return createTicket(webTicket, route, price, totalMinutes, formatedTicketDate, date);
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

        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector("div.main-detail-wrap"), 1));
        try {
            synchronized (driver) {
                driver.wait(5000);
            }
        } catch (InterruptedException e) {
        }

        List<WebElement> elements = driver.findElements(By.cssSelector("div.main-detail-wrap"));
        log.info("INFOBUS TICKETS IN single getTicket(): " + elements.size());
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
                log.info("INFOBUS URL: " + driver.getCurrentUrl());
                break;
            }
        }

        if (ticket.getUrls().getInfobus() != null) {
            emitter.send(SseEmitter.event().name(SiteConstants.INFOBUS).data(ticket.getUrls().getInfobus()));
        }

        driver.quit();
        return CompletableFuture.completedFuture(true);
    }

    private void requestTickets(String departureCity, String arrivalCity, String departureDate, ChromeDriver driver) throws ParseException {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get(linkProps.getInfobus());


        selectCity("city_from", "ui-id-1", departureCity, driver, wait);
        selectCity("city_to", "ui-id-2", arrivalCity, driver, wait);

        selectDate(departureDate, driver, wait);

        driver.findElement(By.id("main_form_search_button")).click();
    }

    private void selectCity(String inputCityId, String clickableElementId, String city, WebDriver driver, WebDriverWait wait) {
        WebElement inputCity = driver.findElement(By.id(inputCityId));
        inputCity.click();
        inputCity.click();
        inputCity.sendKeys(city);
        wait.until(ExpectedConditions.elementToBeClickable(By.id(clickableElementId))).findElements(By.tagName("li")).get(0).click();
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

    private static Ticket createTicket(WebElement webTicket, Route route, String price, int totalMinutes, SimpleDateFormat formatedTicketDate, Date date) {
        return Ticket.builder()
                .id(UUID.randomUUID())
                .route(route)
                .price(BigDecimal.valueOf((long) Double.parseDouble(price)))
                .placeFrom(webTicket.findElement(By.cssSelector("div.departure")).findElement(By.cssSelector("a.text-g")).getText())
                .placeAt(webTicket.findElement(By.cssSelector("div.arrival")).findElement(By.cssSelector("a.text-g")).getText())
                .travelTime(BigDecimal.valueOf(totalMinutes))
                .departureTime(webTicket.findElement(By.cssSelector("div.departure")).findElement(By.cssSelector("div.day_time")).findElements(By.tagName("span")).get(2).getText())
                .arrivalTime(webTicket.findElement(By.cssSelector("div.arrival")).findElement(By.cssSelector("div.day_time")).findElements(By.tagName("span")).get(2).getText())
                .arrivalDate(formatedTicketDate.format(date))
                .type(TypeTransportEnum.BUS).build();
    }

}
