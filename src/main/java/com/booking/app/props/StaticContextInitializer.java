package com.booking.app.props;

import com.booking.app.props.CurrencyRateProps;
import com.booking.app.util.ExchangeRateUtils;
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
