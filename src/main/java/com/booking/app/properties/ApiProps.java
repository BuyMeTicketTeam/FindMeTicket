package com.booking.app.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("api.key")
@Setter
@Getter
public class ApiProps {
    private String currencyRate;
    private String googleClientId;
}
