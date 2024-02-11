package com.booking.app.services.impl;

import com.booking.app.config.LinkProps;
import com.booking.app.constant.SiteConstants;
import com.booking.app.dto.RequestTicketsDTO;
import com.booking.app.dto.UrlAndPriceDTO;
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
import org.openqa.selenium.interactions.Actions;
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
import java.time.Year;
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

    private final ChromeOptions options;

    private static final String DIV_TICKET = "div.main-detail-wrap";

    private static final String DIV_TICKET_NOT_FOUND = "div.col-sm-12.alert.alert-warning";

    @Async
    public CompletableFuture<Boolean> scrapeTickets(RequestTicketsDTO requestTicketDTO, SseEmitter emitter, Route route, String language) throws ParseException, IOException {
        ChromeDriver driver = new ChromeDriver(options);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        String url = determineBaseUrl(language);
        requestTickets(requestTicketDTO.getDepartureCity(), requestTicketDTO.getArrivalCity(), requestTicketDTO.getDepartureDate(), driver, url, language);

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
                //cringe
                driver.wait(5000);
            }
        } catch (InterruptedException e) {
        }

        List<WebElement> elements = driver.findElements(By.cssSelector(DIV_TICKET));

        log.info("INFOBUS TICKETS IN scrapeTickets(): " + elements.size());

        for (int i = 0; i < elements.size() && i < 150; i++) {
            Ticket ticket = scrapeTicketInfo(elements.get(i), route);
            if (route.getTickets().add(ticket)) {
                emitter.send(SseEmitter.event().name("Infobus bus: ").data(ticketMapper.ticketToTicketDto(ticket, language)));
            }
        }

        driver.quit();
        return CompletableFuture.completedFuture(true);
    }

    @Async
    @Override
    public CompletableFuture<Boolean> getTicket(SseEmitter emitter, Ticket ticket, String language) throws IOException, ParseException {
        ChromeDriver driver = new ChromeDriver(options);

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
                    ticket.getPrice().equals(new BigDecimal(price))) {

                WebElement button = element.findElement(By.cssSelector("button.btn"));
                Actions actions = new Actions(driver);
                actions.moveToElement(button).doubleClick().build().perform();

                wait.until(ExpectedConditions.urlContains("deeplink"));
                ticket.getUrls().setInfobus(driver.getCurrentUrl());
                log.info("INFOBUS URL: " + driver.getCurrentUrl());
                break;
            }
        }

        if (ticket.getUrls().getInfobus() != null) {
            emitter.send(SseEmitter.event().name(SiteConstants.INFOBUS).data(
                    UrlAndPriceDTO.builder()
                            .price(ticket.getPrice())
                            .url(ticket.getUrls().getInfobus())
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
            default -> linkProps.getInfobusUaBus();
        };
    }

    private static Ticket scrapeTicketInfo(WebElement webTicket, Route route) throws ParseException {
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

        return createTicket(webTicket, route, price, totalMinutes, formattedTicketDate, date);
    }


    private static void requestTickets(String departureCity, String arrivalCity, String departureDate, ChromeDriver driver, String url, String language) throws ParseException {
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
        wait.until(ExpectedConditions.elementToBeClickable(By.id(clickableElementId))).findElements(By.tagName("li")).get(0).click();
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

    private static Ticket createTicket(WebElement webTicket, Route route, String price, int totalMinutes, SimpleDateFormat formattedTicketDate, Date date) {
        return Ticket.builder()
                .id(UUID.randomUUID())
                .route(route)
                .price(new BigDecimal(price))
                .placeFrom(webTicket.findElement(By.cssSelector("div.departure")).findElement(By.cssSelector("a.text-g")).getText())
                .placeAt(webTicket.findElement(By.cssSelector("div.arrival")).findElement(By.cssSelector("a.text-g")).getText())
                .travelTime(BigDecimal.valueOf(totalMinutes))
                .departureTime(webTicket.findElement(By.cssSelector("div.departure")).findElement(By.cssSelector("div.day_time")).findElements(By.tagName("span")).get(2).getText())
                .arrivalTime(webTicket.findElement(By.cssSelector("div.arrival")).findElement(By.cssSelector("div.day_time")).findElements(By.tagName("span")).get(2).getText())
                .arrivalDate(formattedTicketDate.format(date))
                .сarrier(webTicket.findElement(By.cssSelector("span.carrier-info")).findElement(By.cssSelector("a.text-g")).getText().toUpperCase().replaceAll("\"", "").replace("ТОВ ", ""))
                .type(TypeTransportEnum.BUS).build();
    }

}
