package com.booking.app.properties;

import com.booking.app.utils.ExchangeRateUtils;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StaticContextInitializer {

    private final CurrencyRateProps currencyRateProps;

    @PostConstruct
    void init() {
        ExchangeRateUtils.setCurrencyRateProps(currencyRateProps);
    }

}
