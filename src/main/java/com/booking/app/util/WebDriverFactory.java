package com.booking.app.util;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebDriverFactory {

    private final ChromeOptions chromeOptions;

    public WebDriver createInstance() {
        WebDriverManager.chromedriver().setup();
        return new ChromeDriver(chromeOptions);
    }

}
