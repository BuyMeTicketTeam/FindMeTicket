package com.booking.app.services.impl;

import com.booking.app.config.LinkProps;
import com.booking.app.constant.SiteConstants;
import com.booking.app.dto.UrlAndPriceDTO;
import com.booking.app.entity.BusTicket;
import com.booking.app.entity.Route;
import com.booking.app.mapper.BusMapper;
import com.booking.app.services.ScraperService;
import com.booking.app.util.ExchangeRateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.Range;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service("busfor")
@RequiredArgsConstructor
@Log4j2
public class BusforScraperServiceImpl implements ScraperService {

    private final LinkProps linkProps;

    private final BusMapper busMapper;

    private final WebDriverFactory webDriverFactory;

    private static final String DIV_TICKET = "div.ticket";

    private static final String DIV_TICKET_NOT_FOUND = "div.Style__EmptyTitle-xljhz5-2.iBjiPF";

    @Async
    @Override
    public CompletableFuture<Boolean> scrapeTickets(SseEmitter emitter, Route route, String language, Boolean doShow) throws IOException {
        WebDriver driver = webDriverFactory.createInstance();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        String url = determineBaseUrl(language);
        String fulfilledUrl = String.format(url, route.getDepartureCity(), route.getArrivalCity(), route.getDepartureDate());

        driver.get(fulfilledUrl);

        try {
            wait.until(ExpectedConditions.or(ExpectedConditions.presenceOfElementLocated(By.cssSelector(DIV_TICKET_NOT_FOUND)), ExpectedConditions.presenceOfElementLocated(By.cssSelector(DIV_TICKET))));
            driver.findElement(By.cssSelector(DIV_TICKET));
        } catch (Exception e) {
            driver.quit();
            log.info("BUSFOR TICKETS IN scrapeTickets(): NOT FOUND");
            return CompletableFuture.completedFuture(false);
        }

        try {
            synchronized (driver) {
                driver.wait(5000);
            }
        } catch (InterruptedException e) {
        }

        List<WebElement> tickets = driver.findElements(By.cssSelector(DIV_TICKET));

        log.info("BUSFOR TICKETS IN scrapeTickets(): " + tickets.size());
        BigDecimal currentUAH = null;
        if (language.equals("eng"))
            currentUAH = ExchangeRateUtils.getCurrentExchangeRate("PLN", "UAH");

        for (int i = 0; i < tickets.size() && i < 150; i++) {

            WebElement webTicket = driver.findElements(By.cssSelector(DIV_TICKET)).get(i);
            BusTicket ticket = scrapeTicketInfo(webTicket, route, currentUAH, language, wait);
            if (route.getTickets().add(ticket)) {
                if (BooleanUtils.isTrue(doShow))
                    emitter.send(SseEmitter.event().name("Busfor bus: ").data(busMapper.ticketToTicketDto(ticket, language)));
            } else {
                route.getTickets().stream()
                        .filter(t -> t.equals(ticket))
                        .findFirst()
                        .ifPresent(t -> ((BusTicket) t).setBusforPrice(ticket.getBusforPrice()));
            }
        }

        driver.quit();
        return CompletableFuture.completedFuture(true);
    }

    @Async
    @Override
    public CompletableFuture<Boolean> getBusTicket(SseEmitter emitter, BusTicket ticket, String language) throws IOException {
        WebDriver driver = webDriverFactory.createInstance();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        String departureCity = ticket.getRoute().getDepartureCity();
        String arrivalCity = ticket.getRoute().getArrivalCity();
        String departureDate = ticket.getRoute().getDepartureDate();

        String url = determineBaseUrl(language);
        String fulfilledUrl = String.format(url, departureCity, arrivalCity, departureDate);

        driver.get(fulfilledUrl);
        try {
            wait.until(ExpectedConditions.or(ExpectedConditions.presenceOfElementLocated(By.cssSelector(DIV_TICKET_NOT_FOUND)), ExpectedConditions.presenceOfElementLocated(By.cssSelector(DIV_TICKET))));
            driver.findElement(By.cssSelector(DIV_TICKET));
        } catch (Exception e) {
            driver.quit();
            log.info("BUSFOR TICKETS IN scrapeTickets(): NOT FOUND");
            return CompletableFuture.completedFuture(false);
        }

        try {
            synchronized (driver) {
                driver.wait(5000);
            }
        } catch (InterruptedException e) {
        }

        List<WebElement> tickets = driver.findElements(By.cssSelector(DIV_TICKET));
        log.info("BUSFOR TICKETS IN single getTicket(): " + tickets.size());
        BigDecimal currentUAH = null;
        if (language.equals("eng"))
            currentUAH = ExchangeRateUtils.getCurrentExchangeRate("PLN", "UAH");
        for (WebElement element : tickets) {

            List<WebElement> ticketInfo = element.findElements(By.cssSelector("div.Style__Item-yh63zd-7.kAreny"));

            WebElement departureInfo = ticketInfo.get(0);
            WebElement arrivalInfo = ticketInfo.get(1);

            String departureDateTime = departureInfo.findElement(By.cssSelector("div.Style__Time-sc-1n9rkhj-0.bmnWRj")).getText();
            String arrivalDateTime = arrivalInfo.findElement(By.cssSelector("div.Style__Time-sc-1n9rkhj-0.bmnWRj")).getText();

            BigDecimal price = new BigDecimal(element.findElement(By.cssSelector("span.price")).getText().replace(",", ".").trim());
            if (language.equals("eng")) price = currentUAH.multiply(price);
            BigDecimal difference = ticket.getBusforPrice().subtract(price);
            Range<Integer> range = Range.between(-2, 2);

            if (range.contains(difference.intValue()) &&
                    ticket.getArrivalTime().equals(arrivalDateTime.substring(0, 5)) &&
                    ticket.getDepartureTime().equals(departureDateTime.substring(0, 5))) {

                WebElement button = wait.until(ExpectedConditions.elementToBeClickable(element.findElement(By.tagName("button")).findElement(By.tagName("span"))));
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);

                wait.until(ExpectedConditions.or(ExpectedConditions.urlContains("preorders"), (ExpectedConditions.urlContains("checkout"))));

                ticket.setBusforLink(driver.getCurrentUrl());
                log.info("BUSFOR URL: " + driver.getCurrentUrl());
                break;
            }
        }

        if (ticket.getBusforLink() != null) {
            emitter.send(SseEmitter.event().name(SiteConstants.BUSFOR_UA).data(UrlAndPriceDTO.builder()
                    .price(ticket.getBusforPrice())
                    .url(ticket.getBusforLink())
                    .build()));
        } else log.info("BUSFOR URL NOT FOUND");

        driver.quit();
        return CompletableFuture.completedFuture(true);
    }

    @Override
    public String determineBaseUrl(String language) throws UndefinedLanguageException {
        return switch (language) {
            case ("ua") -> linkProps.getBusforUaBus();
            case ("eng") -> linkProps.getBusforEngBus();
            default ->
                    throw new UndefinedLanguageException("Incomprehensible language passed into " + HttpHeaders.CONTENT_LANGUAGE);
        };
    }

    private static BusTicket scrapeTicketInfo(WebElement webTicket, Route route, BigDecimal currentRate, String language, WebDriverWait wait) {

        String carrier = webTicket.findElement(By.cssSelector("div.Style__Information-sc-13gvs4g-6.jBuzam > div.Style__Carrier-sc-13gvs4g-3.gUvIjh > span:nth-child(2)")).getText().toUpperCase();

        List<WebElement> ticketInfo = webTicket.findElements(By.cssSelector("div.Style__Item-yh63zd-7.kAreny"));

        WebElement departureInfo = ticketInfo.get(0);
        WebElement arrivalInfo = ticketInfo.get(1);

        String departureDateTime = departureInfo.findElement(By.cssSelector("div.Style__Time-sc-1n9rkhj-0.bmnWRj")).getText();
        String arrivalDateTime = arrivalInfo.findElement(By.cssSelector("div.Style__Time-sc-1n9rkhj-0.bmnWRj")).getText();
        String travelTime = webTicket.findElement(By.cssSelector("span.Style__TimeInRoad-yh63zd-0.btMUVs")).getText();
        travelTime = travelTime.substring(0, travelTime.length() - 9);

        String[] parts = travelTime.split("[^\\d]+");
        int hours = Integer.parseInt(parts[0]);
        int minutes = 0;
        if (parts.length == 2) {
            minutes = Integer.parseInt(parts[1]);
        }
        int totalMinutes = hours * 60 + minutes;

        BigDecimal price = new BigDecimal(webTicket.findElement(By.cssSelector("span.price")).getText().replace(",", ".").trim());
        String arrivalDate = formatDate(arrivalDateTime.substring(6), language);

        if (language.equals("eng"))
            price = currentRate.multiply(price).setScale(2, BigDecimal.ROUND_HALF_UP);

        return createTicket(route, departureInfo, arrivalInfo, departureDateTime, arrivalDateTime.substring(0, 5), arrivalDate, totalMinutes, price, carrier);
    }

    private static String formatDate(String inputDate, String language) {
        DateTimeFormatter formatter;
        DateTimeFormatter resultFormatter;

        return switch (language) {
            case "ua" -> {
                formatter = DateTimeFormatter.ofPattern("d MMM u", new Locale("uk"));
                resultFormatter = DateTimeFormatter.ofPattern("d.MM, EEE", new Locale("uk"));
                LocalDate date = LocalDate.parse(inputDate + " " + Year.now().getValue(), formatter);
                yield date.format(resultFormatter);
            }
            case "eng" -> {
                formatter = DateTimeFormatter.ofPattern("d MMM u", new Locale("en"));
                resultFormatter = DateTimeFormatter.ofPattern("d.MM, EEE", new Locale("en"));
                LocalDate date = LocalDate.parse(inputDate + " " + Year.now().getValue(), formatter);
                yield date.format(resultFormatter);
            }
            default -> {
                formatter = DateTimeFormatter.ofPattern("d MMM u", new Locale("uk"));
                resultFormatter = DateTimeFormatter.ofPattern("d.MM, EEE", new Locale("uk"));
                LocalDate date = LocalDate.parse(inputDate + " " + Year.now().getValue(), formatter);
                yield date.format(resultFormatter);
            }
        };
    }

    private static BusTicket createTicket(Route route, WebElement departureInfo, WebElement
            arrivalInfo, String departureDateTime, String arrivalDateTime, String arrivalDate, int totalMinutes, BigDecimal price, String carrier) {
        return BusTicket.builder()
                .id(UUID.randomUUID())
                .route(route)
                .placeFrom(departureInfo.findElement(By.cssSelector("div.LinesEllipsis")).getText())
                .placeAt(arrivalInfo.findElement(By.cssSelector("div.LinesEllipsis")).getText())
                .departureTime(departureDateTime.substring(0, 5))
                .arrivalTime(arrivalDateTime)
                .arrivalDate(arrivalDate)
                .busforPrice(price)
                .travelTime(BigDecimal.valueOf(totalMinutes))
                .carrier(carrier).build();
    }

}
