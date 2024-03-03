package com.booking.app.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "link")
@RequiredArgsConstructor
@Getter
public final class LinkProps {
    private final String busforUaBus;
    private final String busforEngBus;

    private final String infobusUaBus;
    private final String infobusEngBus;

    private final String infobusEngTrain;
    private final String infobusUaTrain;

    private final String proizdUaBus;
    private final String proizdEngBus;

    private final String proizdEngTrain;
    private final String proizdUaTrain;

}
