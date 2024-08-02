package com.booking.app.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("webdriver.selenium")
@Setter
@Getter
public class WebDriverProps {
    private String url;
}
