package com.booking.app.services.impl;

import com.booking.app.dto.RequestTicketsDTO;
import com.booking.app.dto.TicketDTO;

import com.booking.app.entity.Ticket;
import com.booking.app.mapper.TicketMapper;
import com.booking.app.repositories.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;


@Service
@RequiredArgsConstructor
@Log4j2
public class ScrapingServiceImpl {

    private static final String busforLink = "https://busfor.ua/автобуси/%s/%s?on=%s&passengers=1&search=true";
    private final ChromeDriver driver;
    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;



    public List<TicketDTO> getTicketsFromDB(RequestTicketsDTO requestNextTicketsDTO) throws ParseException {

        List<TicketDTO> result = new LinkedList<>();

        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {

        }

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = inputFormat.parse(requestNextTicketsDTO.getDepartureDate());

        SimpleDateFormat outputFormat = new SimpleDateFormat("dd.MM, E", new Locale("uk", "UA"));
        String formattedDate = outputFormat.format(date);

        log.info(formattedDate);

        List<Ticket> ticketsFromBD = ticketRepository.findByDepartureCityAndArrivalCityAndDepartureDate(requestNextTicketsDTO.getDepartureCity(), requestNextTicketsDTO.getArrivalCity(), formattedDate);

        for (int i = requestNextTicketsDTO.getIndexFrom(); i < requestNextTicketsDTO.getIndexFrom() + 5; i++) {
            if (i >= ticketsFromBD.size()) break;
            result.add(ticketMapper.toTicketDTO(ticketsFromBD.get(i)));
        }

        return result;
    }


    @Async
    public void scrapFromBusfor(RequestTicketsDTO requestTicketDTO) {

        String url = String.format(busforLink, requestTicketDTO.getDepartureCity(), requestTicketDTO.getArrivalCity(), requestTicketDTO.getDepartureDate());

        driver.get(url);

        try {
            synchronized (driver) {
                driver.wait(5000);
            }
        } catch (InterruptedException e) {
        }

        List<WebElement> products = driver.findElements(By.cssSelector("div.ticket"));

        log.info(products.size());

        saveTicketsToDB(url, products.size());

    }

    private static String formatDate(String inputDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM u", new Locale("uk"));
        LocalDate date = LocalDate.parse(inputDate + " 2023", formatter);
        formatter = DateTimeFormatter.ofPattern("dd.MM, EEE", new Locale("uk"));
        return date.format(formatter);
    }


    private void saveTicketsToDB(String busforUrl, int n) {


        for (int i = 0; i < n; i++) {

            driver.get(busforUrl);

            try {
                synchronized (driver) {
                    driver.wait(5000);
                }
            } catch (InterruptedException e) {

            }

            WebElement ticket = driver.findElements(By.cssSelector("div.ticket")).get(i);

            List<WebElement> ticketInfo = ticket.findElements(By.cssSelector("div.Style__Item-yh63zd-7.kAreny"));

            WebElement departureInfo = ticketInfo.get(0);
            WebElement arrivalInfo = ticketInfo.get(1);

            String departureDateTime = departureInfo.findElement(By.cssSelector("div.Style__Time-sc-1n9rkhj-0.bmnWRj")).getText();
            String arrivalDateTime = departureInfo.findElement(By.cssSelector("div.Style__Time-sc-1n9rkhj-0.bmnWRj")).getText();
            String travelTime = ticket.findElement(By.cssSelector("span.Style__TimeInRoad-yh63zd-0.btMUVs")).getText();

            Ticket temp = Ticket.builder()
                    .departureCity(departureInfo.findElement(By.cssSelector("div.Style__Title-yh63zd-5.cspGxb")).getText())
                    .arrivalCity(arrivalInfo.findElement(By.cssSelector("div.Style__Title-yh63zd-5.cspGxb")).getText())
                    .placeFrom(departureInfo.findElement(By.cssSelector("div.LinesEllipsis")).getText())
                    .placeAt(departureInfo.findElement(By.cssSelector("div.LinesEllipsis")).getText())
                    .departureTime(departureDateTime.substring(0, 5))
                    .departureDate(formatDate(departureDateTime.substring(6)))
                    .arrivalTime(arrivalDateTime.substring(0, 5))
                    .arrivalDate(formatDate(arrivalDateTime.substring(6)))
                    .price(ticket.findElement(By.cssSelector("span.price")).getText() + " грн")
                    .travelTime(travelTime.substring(0, travelTime.length() - 9))
                    .addingTime(LocalDateTime.now()).build();

            String url = "";

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.elementToBeClickable(ticket.findElement(By.tagName("button")).findElement(By.tagName("span"))));

            ticket.findElement(By.tagName("button")).findElement(By.tagName("span")).click();

            wait.until(ExpectedConditions.urlContains("preorders"));

            url = driver.getCurrentUrl();


            temp.setUrl(url);

            log.info(temp.toString());

            ticketRepository.save(temp);
        }

    }

    @Async
    @Scheduled(fixedRate = 3600000)
    public void deleteOldTickets() {
        LocalDateTime fifteenMinutesAgo = LocalDateTime.now().minusMinutes(60);

        ticketRepository.deleteOlderThan(fifteenMinutesAgo);
    }
}
