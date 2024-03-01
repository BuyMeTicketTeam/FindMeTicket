package com.booking.app.services.impl;

import com.booking.app.config.LinkProps;
import com.booking.app.constant.SiteConstants;
import com.booking.app.dto.UrlAndPriceDTO;
import com.booking.app.entity.BusTicket;
import com.booking.app.entity.Route;
import com.booking.app.entity.Ticket;
import com.booking.app.exception.exception.UndefinedLanguageException;
import com.booking.app.mapper.BusMapper;
import com.booking.app.repositories.BusTicketRepository;
import com.booking.app.services.ScraperService;
import com.booking.app.services.TicketOperation;
import com.booking.app.util.WebDriverFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.BooleanUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Year;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service("infobus")
@RequiredArgsConstructor
@Log4j2
public class InfobusScraperServiceImpl implements ScraperService, TicketOperation {

    private final LinkProps linkProps;

    private final BusMapper busMapper;

    private final WebDriverFactory webDriverFactory;

    private final BusTicketRepository repository;

    private static final String DIV_TICKET = "div.main-detail-wrap";

    private static final String DIV_TICKET_NOT_FOUND = "div.col-sm-12.alert.alert-warning";

    @Async
    public CompletableFuture<Boolean> scrapeTickets(SseEmitter emitter, Route route, String language, Boolean doShow) throws ParseException, IOException {
        WebDriver driver = webDriverFactory.createInstance();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        String url = determineBaseUrl(language);
        requestTickets(route.getDepartureCity(), route.getArrivalCity(), route.getDepartureDate(), driver, url, language);

        try {
            wait.until(ExpectedConditions.or(ExpectedConditions.presenceOfElementLocated(By.cssSelector(DIV_TICKET_NOT_FOUND)), ExpectedConditions.presenceOfElementLocated(By.cssSelector(DIV_TICKET))));
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
            BusTicket scrapedTicket = scrapeTicketInfo(elements.get(i), route);

            Optional<BusTicket> busTicket = repository.findByDepartureTimeAndArrivalTimeAndArrivalDateAndCarrier(scrapedTicket.getDepartureTime(), scrapedTicket.getArrivalTime(), scrapedTicket.getArrivalDate(), scrapedTicket.getCarrier());

            if (busTicket.isEmpty())
                saveTicket(emitter, route, language, doShow, scrapedTicket);

            if (busTicket.isPresent() && Objects.isNull(busTicket.get().getInfobusPrice()))
                updateTicket(busTicket.get(), scrapedTicket);
        }

        driver.quit();
        return CompletableFuture.completedFuture(true);
    }

    @Async
    @Override
    public CompletableFuture<Boolean> getBusTicket(SseEmitter emitter, BusTicket ticket, String language) throws IOException, ParseException {
        WebDriver driver = webDriverFactory.createInstance();

        Route route = ticket.getRoute();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        String url = determineBaseUrl(language);
        requestTickets(route.getDepartureCity(), route.getArrivalCity(), route.getDepartureDate(), driver, url, language);

        try {
            wait.until(ExpectedConditions.or(ExpectedConditions.presenceOfElementLocated(By.cssSelector(DIV_TICKET_NOT_FOUND)), ExpectedConditions.presenceOfElementLocated(By.cssSelector(DIV_TICKET))));

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
        log.info("INFOBUS TICKETS IN single getTicket(): " + elements.size());
        for (WebElement element : elements) {
            String price = element.findElement(By.cssSelector("span.price-number")).getText().replace(" UAH", "");

            String departureTime = element.findElement(By.cssSelector("div.departure")).findElement(By.cssSelector("div.day_time")).findElements(By.tagName("span")).get(2).getText();

            String arrivalTime = element.findElement(By.cssSelector("div.arrival")).findElement(By.cssSelector("div.day_time")).findElements(By.tagName("span")).get(2).getText();

            if (ticket.getDepartureTime().equals(departureTime) &&
                    ticket.getArrivalTime().equals(arrivalTime) &&
                    ticket.getInfobusPrice().equals(new BigDecimal(price))) {

                WebElement button = element.findElement(By.cssSelector("button.btn"));
                Actions actions = new Actions(driver);
                actions.moveToElement(button).doubleClick().build().perform();

                wait.until(ExpectedConditions.urlContains("deeplink"));
                ticket.setInfobusLink(driver.getCurrentUrl());
                log.info("INFOBUS URL: " + driver.getCurrentUrl());
                break;
            }
        }

        if (ticket.getInfobusLink() != null) {
            emitter.send(SseEmitter.event().name(SiteConstants.INFOBUS).data(
                    UrlAndPriceDTO.builder()
                            .price(ticket.getInfobusPrice())
                            .url(ticket.getInfobusLink())
                            .build()));
        } else log.info("INFOBUS URL NOT FOUND");

        driver.quit();
        return CompletableFuture.completedFuture(true);
    }

    @Override
    public String determineBaseUrl(String language) {
        return switch (language) {
            case ("ua") -> linkProps.getInfobusUaBus();
            case ("eng") -> linkProps.getInfobusEngBus();
            default ->
                    throw new UndefinedLanguageException("Incomprehensible language passed into " + HttpHeaders.CONTENT_LANGUAGE);
        };
    }

    @Override
    public void saveTicket(SseEmitter emitter, Route route, String language, Boolean doDisplay, Ticket scrapedTicket) throws IOException {
        scrapedTicket.setRoute(route);
        repository.save((BusTicket) scrapedTicket);
        if (BooleanUtils.isTrue(doDisplay))
            emitter.send(SseEmitter.event().name("Infobus bus: ").data(busMapper.ticketToTicketDto((BusTicket) scrapedTicket, language)));
    }

    @Override
    public void updateTicket(Ticket ticket, Ticket scrapedTicket) {
        ((BusTicket) ticket).updateInfobusPrice(((BusTicket) scrapedTicket).getInfobusPrice());
        repository.save((BusTicket) ticket);
    }

    private static BusTicket scrapeTicketInfo(WebElement webTicket, Route route) throws ParseException {
        SimpleDateFormat ticketDate = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat formattedTicketDate = new SimpleDateFormat("dd.MM, E", new Locale("uk"));

        String arrivalDate = webTicket.findElements(By.cssSelector("span.day-preffix")).get(1).getText().substring(3) + "." + Year.now().getValue();

        Date date = ticketDate.parse(arrivalDate);

        String price = webTicket.findElement(By.cssSelector("span.price-number")).getText().replace(" UAH", "");
        String travelTime = webTicket.findElement(By.className("duration-time")).getText().toLowerCase().replace(" г", "год.").replace(" хв", "хв");

        String[] parts = travelTime.split("[^\\d]+");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        int totalMinutes = hours * 60 + minutes;

        String carrier = webTicket.findElement(By.cssSelector("span.carrier-info")).findElement(By.cssSelector("a.text-g")).getText();

        if(carrier.isEmpty()){
            carrier = webTicket.findElement(By.cssSelector("span.carrier-info")).findElement(By.cssSelector("a.text-g")).getAttribute("href").replaceAll(".*/", "");
        }

        carrier.toUpperCase().replaceAll("\"", "").replace("ТОВ ", "");

        return createTicket(webTicket, route, price, carrier, totalMinutes, formattedTicketDate, date);
    }


    private static void requestTickets(String departureCity, String arrivalCity, String departureDate, WebDriver driver, String url, String language) throws ParseException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        driver.get(url);

        selectCity("city_from", "ui-id-1", departureCity, driver, wait);
        selectCity("city_to", "ui-id-2", arrivalCity, driver, wait);

        selectDate(departureDate, driver, wait, language);

        driver.findElement(By.id("main_form_search_button")).click();
    }

    private static void selectCity(String inputCityId, String clickableElementId, String city, WebDriver driver, WebDriverWait wait) {
        WebElement inputCity = driver.findElement(By.id(inputCityId));
        Actions actions = new Actions(driver);
        actions.moveToElement(inputCity).doubleClick().build().perform();
        inputCity.sendKeys(city);
        wait.until(ExpectedConditions.elementToBeClickable(By.id(clickableElementId))).findElements(By.tagName("li")).getFirst().click();
    }

    private static void selectDate(String departureDate, WebDriver driver, WebDriverWait wait, String language) throws ParseException {
        WebElement dateFrom = driver.findElement(By.id("dateFrom"));
        dateFrom.click();

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outputMonthFormat = new SimpleDateFormat("MMMM", new Locale("uk"));
        SimpleDateFormat outputYearFormat = new SimpleDateFormat("yyyy", new Locale("uk", "en"));
        SimpleDateFormat outputDayFormat = new SimpleDateFormat("d", new Locale("uk", "en"));

        if (language.equals("eng")) outputMonthFormat = new SimpleDateFormat("MMMM", new Locale("en"));
        if (language.equals("eng")) outputYearFormat = new SimpleDateFormat("yyyy", new Locale("en"));
        if (language.equals("eng")) outputDayFormat = new SimpleDateFormat("d", new Locale("en"));

        Date inputDate = inputFormat.parse(departureDate);

        String requestMonth = outputMonthFormat.format(inputDate);
        String requestYear = outputYearFormat.format(inputDate);
        String requestDay = outputDayFormat.format(inputDate);

        String calendarMonth = driver.findElement(By.cssSelector("span.ui-datepicker-month")).getText();
        String calendarYear = driver.findElement(By.cssSelector("span.ui-datepicker-year")).getText();

        while (!(calendarMonth.equalsIgnoreCase(requestMonth) && calendarYear.equals(requestYear))) {
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@data-handler=\"next\"]"))).click();

            calendarMonth = driver.findElement(By.cssSelector("span.ui-datepicker-month")).getText();
            calendarYear = driver.findElement(By.cssSelector("span.ui-datepicker-year")).getText();
        }

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a.ui-state-default")));
        WebElement element = driver.findElements(By.cssSelector("a.ui-state-default")).stream().filter(e -> e.getText().equals(requestDay)).findFirst().get();
        Actions actions = new Actions(driver);
        actions.moveToElement(element).click().build().perform();
    }

    private static BusTicket createTicket(WebElement webTicket, Route route, String price, String carrier, int totalMinutes, SimpleDateFormat formattedTicketDate, Date date) {
        return BusTicket.builder()
                .id(UUID.randomUUID())
                .route(route)
                .infobusPrice(new BigDecimal(price))
                .placeFrom(webTicket.findElement(By.cssSelector("div.departure")).findElement(By.cssSelector("a.text-g")).getText())
                .placeAt(webTicket.findElement(By.cssSelector("div.arrival")).findElement(By.cssSelector("a.text-g")).getText())
                .travelTime(BigDecimal.valueOf(totalMinutes))
                .departureTime(webTicket.findElement(By.cssSelector("div.departure")).findElement(By.cssSelector("div.day_time")).findElements(By.tagName("span")).get(2).getText())
                .arrivalTime(webTicket.findElement(By.cssSelector("div.arrival")).findElement(By.cssSelector("div.day_time")).findElements(By.tagName("span")).get(2).getText())
                .arrivalDate(formattedTicketDate.format(date))
                .carrier(carrier).build();
    }

}
