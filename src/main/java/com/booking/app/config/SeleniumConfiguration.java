package com.booking.app.config;

import jakarta.annotation.PostConstruct;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SeleniumConfiguration {

    @PostConstruct
    void postConstruct(){
        System.setProperty("webdriver.chrome.driver", "D:\\chromedriver-win32");
    }

    @Bean
    public ChromeDriver driver(){
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--headless");
        return new ChromeDriver(options);
    }
}
