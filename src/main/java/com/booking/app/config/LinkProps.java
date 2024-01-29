package com.booking.app.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("link")
@RequiredArgsConstructor
@Getter
public class LinkProps {
    private final String busfor;

    private final String infobus;

    private final String proizd;

}
