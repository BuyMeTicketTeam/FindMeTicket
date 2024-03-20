package com.booking.app.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("api")
@Setter
@Getter
public class CurrencyRateProps {
    private String currencyRateKey;
}
