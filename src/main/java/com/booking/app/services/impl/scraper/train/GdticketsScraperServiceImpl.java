package com.booking.app.services.impl.scraper.train;

import com.booking.app.entities.ticket.Route;
import com.booking.app.entities.ticket.bus.BusTicket;
import com.booking.app.mappers.BusMapper;
import com.booking.app.properties.LinkProps;
import com.booking.app.repositories.BusTicketRepository;
import com.booking.app.services.ScraperService;
import com.booking.app.services.impl.scraper.WebDriverFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.mutable.MutableBoolean;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

@Service("gdtickets")
@RequiredArgsConstructor
@Log4j2
public class GdticketsScraperServiceImpl implements ScraperService {

    private final LinkProps linkProps;

    private final BusMapper busMapper;

    private final WebDriverFactory webDriverFactory;

    private final BusTicketRepository repository;

    private static final String DIV_TICKET = "div.main-detail-wrap";

    private static final String DIV_TICKET_NOT_FOUND = "div.col-sm-12.alert.alert-warning";

    @Async
    @Override
    public CompletableFuture<Boolean> scrapeTickets(SseEmitter emitter, Route route, String language, Boolean doShow, MutableBoolean emitterNotExpired) throws ParseException, IOException {
        WebDriver driver = webDriverFactory.createInstance();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
//        String url = determineBaseUrl(language);
        if (!requestTickets(route.getDepartureCity(), route.getArrivalCity(), route.getDepartureDate(), driver, "https://gd.tickets.ua/", language))
            return CompletableFuture.completedFuture(false);
        //        try {
//            wait.until(ExpectedConditions.or(ExpectedConditions.presenceOfElementLocated(By.cssSelector(DIV_TICKET_NOT_FOUND)), ExpectedConditions.presenceOfElementLocated(By.cssSelector(DIV_TICKET))));
//            driver.findElement(By.cssSelector(DIV_TICKET));
//        } catch (Exception e) {
//            driver.quit();
//            log.info("INFOBUS TICKETS IN scrapeTickets(): NOT FOUND");
//            return CompletableFuture.completedFuture(false);
//        }
//
//        try {
//            synchronized (driver) {
//                driver.wait(5000);
//            }
//        } catch (InterruptedException e) {
//        }
//
//        List<WebElement> elements = driver.findElements(By.cssSelector(DIV_TICKET));
//
//        log.info("INFOBUS TICKETS IN scrapeTickets(): " + elements.size());

        return null;
    }

    private static boolean requestTickets(String departureCity, String arrivalCity, String departureDate, WebDriver driver, String url, String language) throws ParseException {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        driver.get(url);

        selectCity("body > header > div > div.app-main-search-form.mb-4.mb-xl-10 > div.mt-2.seach-form-wrapper.search-form-railway > form > div.col-24.mb-2.mb-m-0.col-xl-21 > div.form-shadow.bg-6.border-radius-4 > div > div.col-24.col-xl-16.border-wb-1.border-wb-xl-0.border-color-6.search-form-autocomplete.row.no-gap.f--no-wrap > div.t-menu-v2.t-menu-list.search-form-autocomplete-item.col-24.col-m-12.border-wb-1.border-wb-m-0.border-wr-m-1.border-color-6 > label > div > input", "body > header > div > div.app-main-search-form.mb-4.mb-xl-10 > div.mt-2.seach-form-wrapper.search-form-railway > form > div.col-24.mb-2.mb-m-0.col-xl-21 > div.form-shadow.bg-6.border-radius-4 > div > div.col-24.col-xl-16.border-wb-1.border-wb-xl-0.border-color-6.search-form-autocomplete.row.no-gap.f--no-wrap > div.t-menu-v2.open.t-menu-list.search-form-autocomplete-item.col-24.col-m-12.border-wb-1.border-wb-m-0.border-wr-m-1.border-color-6 > div > div > div.overflow-h-x", departureCity, driver, wait);

        selectCity("body > header > div > div.app-main-search-form.mb-4.mb-xl-10 > div.mt-2.seach-form-wrapper.search-form-railway > form > div.col-24.mb-2.mb-m-0.col-xl-21 > div.form-shadow.bg-6.border-radius-4 > div > div.col-24.col-xl-16.border-wb-1.border-wb-xl-0.border-color-6.search-form-autocomplete.row.no-gap.f--no-wrap > div:nth-child(3) > label > div > input", "body > header > div > div.app-main-search-form.mb-4.mb-xl-10 > div.mt-2.seach-form-wrapper.search-form-railway > form > div.col-24.mb-2.mb-m-0.col-xl-21 > div.form-shadow.bg-6.border-radius-4 > div > div.col-24.col-xl-16.border-wb-1.border-wb-xl-0.border-color-6.search-form-autocomplete.row.no-gap.f--no-wrap > div.t-menu-v2.open.t-menu-list.search-form-autocomplete-item.col-24.col-m-12 > div > div > div.overflow-h-x", arrivalCity, driver, wait);

        if (!selectDate(departureDate, driver, wait, language)) {
            return false;
        }

        Actions actions = new Actions(driver);
        actions.moveToElement(driver.findElement(By.xpath("/html/body/header/div/div[3]/div[6]/form/div[2]/div/div[2]/button"))).click().build().perform();
        return true;
    }

    private static void selectCity(String firstSelector, String secondSelector, String city, WebDriver driver, WebDriverWait wait) {
        WebElement inputCity = driver.findElement(By.cssSelector(firstSelector));
        Actions actions = new Actions(driver);
        actions.moveToElement(inputCity).doubleClick().build().perform();
        inputCity.sendKeys(city);
        try {
            synchronized (driver) {
                driver.wait(500);
            }
        } catch (InterruptedException e) {
        }
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(secondSelector))).findElements(By.tagName("button")).get(0).click();
    }

    private static boolean selectDate(String departureDate, WebDriver driver, WebDriverWait wait, String language) throws ParseException {
        WebElement dateFrom = driver.findElement(By.cssSelector("body > header > div > div.app-main-search-form.mb-4.mb-xl-10 > div.mt-2.seach-form-wrapper.search-form-railway > form > div.col-24.mb-2.mb-m-0.col-xl-21 > div.form-shadow.bg-6.border-radius-4 > div > div.col-24.col-xl-8.border-wl-xl-1.border-color-6.t-menu-v2.open.search-form-calendar > div.search-form-calendar-activators.row.no-gap > div.col-12.border-wr-1.border-color-6 > label > div > input"));
        dateFrom.click();

        try {
            synchronized (driver) {
                driver.wait(1000);
            }
        } catch (InterruptedException e) {
        }


        SimpleDateFormat inputFormat = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat outputMonthFormat = new SimpleDateFormat("MMMM", new Locale("uk"));
        SimpleDateFormat outputYearFormat = new SimpleDateFormat("yyyy", new Locale("uk", "en"));
        SimpleDateFormat outputDayFormat = new SimpleDateFormat("d", new Locale("uk", "en"));

        if (language.equals("eng")) outputMonthFormat = new SimpleDateFormat("MMMM", new Locale("en"));
        if (language.equals("eng")) outputYearFormat = new SimpleDateFormat("yyyy", new Locale("en"));
        if (language.equals("eng")) outputDayFormat = new SimpleDateFormat("d", new Locale("en"));

        Date inputDate = inputFormat.parse(departureDate);

        String requestMonth = outputMonthFormat.format(inputDate);
        int requestMonthNumber = inputDate.getMonth();

        String requestYear = outputYearFormat.format(inputDate);
        String requestDay = outputDayFormat.format(inputDate);

        String firstDateSelector = "body > header > div > div.app-main-search-form.mb-4.mb-xl-10 > div.mt-2.seach-form-wrapper.search-form-railway > form > div.col-24.mb-2.mb-m-0.col-xl-21 > div > div > div.col-24.col-xl-8.border-wl-xl-1.border-color-6.t-menu-v2.open.search-form-calendar > div.t-menu-v2__content.ltr.top.theme-default.border-radius-8.bs-5 > div.search-form-calendar-body.mb-1 > div > div > div:nth-child(1) > div.search-form-calendar-body-month-name.f-center-center.rel.size-5 > div";

        String secondDateSelector = "/html/body/header/div/div[3]/div[6]/form/div[1]/div[1]/div/div[2]/div[2]/div[2]/div/div/div[2]/div[1]/div";

        String calendarYear = driver.findElement(By.cssSelector(firstDateSelector)).getText().replaceAll(".* ", "");

        String firstMonth = driver.findElement(By.cssSelector(firstDateSelector)).getText().replaceAll(" .*", "").toLowerCase();
        String secondMonth = driver.findElement(By.xpath(secondDateSelector)).getText().replaceAll(" .*", "").toLowerCase();

        if (!((firstMonth.equals(requestMonth) || secondMonth.equals(requestMonth)) && calendarYear.equals(requestYear))) {
            return false;
        } else {

            WebElement sdsd = driver.findElement(By.xpath("/html/body/header/div/div[3]/div[6]/form/div[1]/div[1]/div/div[2]/div[2]"));

            List<WebElement> days = (requestMonthNumber % 2 == 0 ? driver.findElement(By.xpath("/html/body/header/div/div[3]/div[6]/form/div[1]/div[1]/div/div[2]/div[2]/div[2]/div/div/div[1]")) : driver.findElement(By.xpath("/html/body/header/div/div[3]/div[6]/form/div[1]/div[1]/div/div[2]/div[2]/div[2]/div/div/div[2]"))).findElements(By.cssSelector("div.f-center-stretch")).stream().map(t -> t.findElements(By.cssSelector("div.f--grow.f--shrink.f--no-basis"))).flatMap(List::stream).toList();

            for (WebElement t : days) {
                try {
                    WebElement element = t.findElement(By.tagName("button")).findElements(By.tagName("div")).get(0);
                    if (element.getText().equals(requestDay)) {
                        Actions actions = new Actions(driver);
                        actions.moveToElement(t.findElement(By.tagName("button"))).click().build().perform();
                        break;
                    }

                } catch (Exception e) {
                }
            }


            return true;
        }
    }

    @Override
    public CompletableFuture<Boolean> scrapeTicketUri(SseEmitter emitter, BusTicket ticket, String language, MutableBoolean emitterNotExpired) throws IOException, ParseException {
        throw new java.lang.UnsupportedOperationException();
    }

    public String determineBaseUri(String language) {
        throw new java.lang.UnsupportedOperationException();
    }
}
