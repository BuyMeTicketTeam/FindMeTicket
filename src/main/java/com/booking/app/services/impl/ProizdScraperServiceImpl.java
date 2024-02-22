package com.booking.app.services.impl;

import com.booking.app.config.LinkProps;
import com.booking.app.constant.SiteConstants;
import com.booking.app.dto.UrlAndPriceDTO;
import com.booking.app.entity.BusTicket;
import com.booking.app.entity.Route;
import com.booking.app.mapper.BusMapper;
import com.booking.app.services.ScraperService;
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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service("proizd")
@RequiredArgsConstructor
@Log4j2
public class ProizdScraperServiceImpl implements ScraperService {

    private final LinkProps linkProps;

    private final BusMapper busMapper;

    private final WebDriverFactory webDriverFactory;

    private static final String DIV_TICKET = "div.trip-adaptive";

    private static final String DIV_TICKET_NOT_FOUND = "div.error.card";

    @Async
    @Override
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
            log.info("PROIZD TICKETS IN scrapeTickets(): NOT FOUND");
            return CompletableFuture.completedFuture(false);
        }

        try {
            synchronized (driver) {
                driver.wait(5000);
            }
        } catch (InterruptedException e) {
        }

        List<WebElement> elements = driver.findElements(By.cssSelector(DIV_TICKET));


        log.info("PROIZD TICKETS IN scrapeTickets(): " + elements.size());


        for (int i = 0; i < elements.size() && i < 150; i++) {
            BusTicket ticket = scrapeTicketInfo(elements.get(i), route, language);
            if (route.getTickets().add(ticket)) {
                if (BooleanUtils.isTrue(doShow))
                    emitter.send(SseEmitter.event().name("Proizd bus: ").data(busMapper.ticketToTicketDto(ticket, language)));
            } else {
                route.getTickets().stream()
                        .filter(t -> t.equals(ticket))
                        .findFirst()
                        .ifPresent(t -> ((BusTicket) t).setProizdPrice(ticket.getProizdPrice()));
            }
        }

        driver.quit();
        return CompletableFuture.completedFuture(true);
    }

    @Async
    @Override
    public CompletableFuture<Boolean> getBusTicket(SseEmitter emitter, BusTicket ticket, String language) throws IOException, ParseException {
        WebDriver driver = webDriverFactory.createInstance();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        Route route = ticket.getRoute();
        String url = determineBaseUrl(language);
        requestTickets(route.getDepartureCity(), route.getArrivalCity(), route.getDepartureDate(), driver, url, language);

        try {
            wait.until(ExpectedConditions.or(ExpectedConditions.presenceOfElementLocated(By.cssSelector(DIV_TICKET_NOT_FOUND)), ExpectedConditions.presenceOfElementLocated(By.cssSelector(DIV_TICKET))));
            driver.findElement(By.cssSelector(DIV_TICKET));
        } catch (Exception e) {
            driver.quit();
            log.info("PROIZD TICKETS IN scrapeTickets(): NOT FOUND");
            return CompletableFuture.completedFuture(false);
        }
        try {
            synchronized (driver) {
                driver.wait(5000);
            }
        } catch (InterruptedException e) {
        }

        List<WebElement> elements = driver.findElements(By.cssSelector(DIV_TICKET));
        log.info("PROIZD TICKETS IN single getTicket(): " + elements.size());
        for (WebElement element : elements) {

            String price = element.findElement(By.cssSelector("div.carriage-bus__price")).getText();
            if (language.equals("ua")) price = price.replace("ГРН", "");
            else price = price.replace("UAH", "");


            String departureTime = element.findElements(By.cssSelector("div.trip__time")).get(0).getText();
            String arrivalTime = element.findElements(By.cssSelector("div.trip__time")).get(1).getText();

            if (ticket.getDepartureTime().equals(departureTime) &&
                    ticket.getArrivalTime().equals(arrivalTime) &&
                    ticket.getProizdPrice().equals(new BigDecimal(price))) {
                ticket.setProizdLink(element.findElement(By.cssSelector("a.btn")).getAttribute("href"));
                log.info("PROIZD URL: " + element.findElement(By.cssSelector("a.btn")).getAttribute("href"));
                break;
            }
        }

        if (ticket.getProizdLink() != null) {
            emitter.send(SseEmitter.event().name(SiteConstants.PROIZD_UA).data(UrlAndPriceDTO.builder()
                    .price(ticket.getProizdPrice())
                    .url(ticket.getProizdLink())
                    .build()));
        } else log.info("PROIZD URL NOT FOUND");

        driver.quit();
        return CompletableFuture.completedFuture(true);
    }


    @Override
    public String determineBaseUrl(String language) {
        return switch (language) {
            case ("ua") -> linkProps.getProizdUaBus();
            case ("eng") -> linkProps.getProizdEngBus();
            default -> linkProps.getProizdUaBus();
        };
    }


    private static BusTicket scrapeTicketInfo(WebElement element, Route route, String language) {
        String arrivalDate = element.findElements(By.cssSelector("div.trip__date")).get(1).getText();
        arrivalDate = arrivalDate.substring(4);

        DateTimeFormatter ticketDate;
        ticketDate = language.equals("eng") ? DateTimeFormatter.ofPattern("MMMM dd yyyy", new Locale("en"))
                : DateTimeFormatter.ofPattern("dd MMMM yyyy", new Locale("uk"));

        LocalDate date = LocalDate.parse(arrivalDate.trim() + " " + Year.now().getValue(), ticketDate);

        ticketDate = language.equals("eng") ? DateTimeFormatter.ofPattern("d.MM, EE", new Locale("en"))
                : DateTimeFormatter.ofPattern("d.MM, EE", new Locale("uk"));
        String formattedDate = date.format(ticketDate);

        String price = element.findElement(By.cssSelector("div.carriage-bus__price")).getText();
        price = language.equals("eng") ? price.replace("UAH", "")
                : price.replace("ГРН", "");

        String travelTime = element.findElement(By.cssSelector("div.travel-time__value")).getText();

        String[] parts = travelTime.split("[^\\d]+");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        int totalMinutes = hours * 60 + minutes;

        String carrier = "";
        try {
            carrier = element.findElement(By.cssSelector("div.bus-info__header > div.transporter > div.transporter__name")).getText().toUpperCase();
        } catch (Exception e) {

        }
        if (carrier.isEmpty()) carrier = "LIKEBUS";
        return createTicket(element, route, price, totalMinutes, formattedDate, carrier);
    }

    private static void requestTickets(String departureCity, String arrivalCity, String departureDate, WebDriver driver, String url, String language) throws ParseException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        Actions actions = new Actions(driver);
        driver.get(url);
        if (language.equals("ua"))
            selectCity(wait, departureCity, "//input[@placeholder='Станція відправлення']", driver);
        else
            selectCity(wait, departureCity, "//input[@placeholder='Departure station']", driver);

        if (language.equals("ua"))
            selectCity(wait, arrivalCity, "//input[@placeholder='Станція прибуття']", driver);
        else
            selectCity(wait, arrivalCity, "//input[@placeholder='Arrival station']", driver);

        selectDate(departureDate, driver, wait, language);

        WebElement element = driver.findElement(By.cssSelector("button.btn.search-form__btn"));
        actions.moveToElement(element).click().build().perform();
    }

    private static void selectCity(WebDriverWait wait, String city, String inputXpath, WebDriver driver) {
        WebElement inputCity = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(inputXpath)));
        Actions actions = new Actions(driver);
        actions.moveToElement(inputCity).click().build().perform();
        inputCity.clear();
        inputCity.sendKeys(city);
        WebElement proposedCity = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//li[@class='station-item active ng-star-inserted']")));
        wait.until(ExpectedConditions.elementToBeClickable(proposedCity)).click();
    }

    private static void selectDate(String departureDate, WebDriver driver, WebDriverWait wait, String language) throws ParseException {
        WebElement dateFrom = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div.search-form__field.search-form__date")));
        Actions actions = new Actions(driver);
        actions.moveToElement(dateFrom).doubleClick().build().perform();

        String calendarMonth = driver.findElement(By.cssSelector("li.calmonth")).getText();

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");


        SimpleDateFormat outputMonthFormat = language.equals("eng") ? new SimpleDateFormat("MMMM", new Locale("en"))
                : new SimpleDateFormat("MMMM", new Locale("uk"));

        SimpleDateFormat outputYearFormat = language.equals("eng") ? new SimpleDateFormat("yyyy", new Locale("en"))
                : new SimpleDateFormat("yyyy", new Locale("uk"));

        SimpleDateFormat outputDayFormat = language.equals("eng") ? new SimpleDateFormat("d", new Locale("en"))
                : new SimpleDateFormat("d", new Locale("uk"));

        Date inputDate = inputFormat.parse(departureDate);

        String requestMonth = outputMonthFormat.format(inputDate);
        String requestYear = outputYearFormat.format(inputDate);
        String requestDay = outputDayFormat.format(inputDate);

        calendarMonth = calendarMonth.substring(0, calendarMonth.length() - 5);

        String calendarYear = driver.findElement(By.cssSelector("li.calmonth")).getText();
        calendarYear = calendarYear.substring(calendarYear.length() - 4);

        while (!(calendarMonth.equalsIgnoreCase(requestMonth) && calendarYear.equals(requestYear))) {

            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("li.calnextmonth"))).click();

            calendarMonth = driver.findElement(By.cssSelector("li.calmonth")).getText();
            calendarMonth = calendarMonth.substring(0, calendarMonth.length() - 5);

            calendarYear = driver.findElement(By.cssSelector("li.calmonth")).getText();
            calendarYear = calendarYear.substring(calendarYear.length() - 4);
        }

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div.calbody")));

        List<WebElement> dates = driver.findElement(By.cssSelector("div.calbody")).findElements(By.tagName("li"));
        int indexOfFirstDay = dates.indexOf(dates.stream().filter(el -> el.getText().equals("1")).findFirst().orElse(null));
        List<WebElement> filteredLi = dates.subList(indexOfFirstDay, dates.size());
        WebElement liDate = filteredLi.stream().filter(el -> el.getText().equals(requestDay)).findFirst().orElse(null);
        actions.moveToElement(liDate).doubleClick().build().perform();

    }

    private static BusTicket createTicket(WebElement element, Route route, String price, int totalMinutes, String formattedTime, String carrier) {
        return BusTicket.builder()
                .id(UUID.randomUUID())
                .route(route)
                .proizdPrice(new BigDecimal(price))
                .placeFrom(element.findElements(By.cssSelector("div.trip__station-address")).get(0).getText())
                .placeAt(element.findElements(By.cssSelector("div.trip__station-address")).get(1).getText())
                .travelTime(BigDecimal.valueOf(totalMinutes))
                .departureTime(element.findElements(By.cssSelector("div.trip__time")).get(0).getText())
                .arrivalTime(element.findElements(By.cssSelector("div.trip__time")).get(1).getText())
                .arrivalDate(formattedTime)
                .carrier(carrier).build();
    }

}
