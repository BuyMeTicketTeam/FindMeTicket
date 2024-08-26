package com.booking.app.services.impl.scraper;

import io.github.bonigarcia.wdm.WebDriverManager;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.NoSuchDriverException;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;

@Component
@RequiredArgsConstructor
@Log4j2
public class WebDriverFactory {

    private final ChromeOptions chromeOptions;

    private final Environment environment;

    @Value("${webdriver.selenium.url}")
    private String dockerDriverUrl;

    @PostConstruct
    void setupDriver() {
        WebDriverManager.chromedriver().setup();
    }

    public WebDriver createInstance() {
        if (environment.matchesProfiles("docker")) {
            try {
                return new RemoteWebDriver(new URL(dockerDriverUrl), chromeOptions);
            } catch (MalformedURLException exception) {
                log.error("Docker WebDriver URL is malformed: {}", exception.getMessage());
                throw new NoSuchDriverException("Failed to create RemoteWebDriver", exception);
            }
        } else {
            return new ChromeDriver(chromeOptions);
        }
    }

}
