package com.booking.app.util;

import io.github.bonigarcia.wdm.WebDriverManager;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;

@Service
@RequiredArgsConstructor
public class WebDriverFactory {

    private final ChromeOptions chromeOptions;

    private final Environment environment;

    @PostConstruct
    void setupDriver() {
        WebDriverManager.chromedriver().setup();
    }

    public WebDriver createInstance() throws MalformedURLException {
        if (environment.matchesProfiles("docker")) {
            return new RemoteWebDriver(new URL("http://selenium-hub:4444"), chromeOptions);
        } else {
            return new ChromeDriver(chromeOptions);
        }
    }

}
