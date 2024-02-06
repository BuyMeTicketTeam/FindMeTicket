package com.booking.app.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("link")
@RequiredArgsConstructor
@Getter
public class LinkProps {
    private final String busforUa;
    private final String busforEng;

    private final String infobusUa;
    private final String infobusEng;

    private final String proizdUa;
    private final String proizdEng;

}
