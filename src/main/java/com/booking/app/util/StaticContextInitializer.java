package com.booking.app.util;

import com.booking.app.props.CurrencyRateProps;
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
