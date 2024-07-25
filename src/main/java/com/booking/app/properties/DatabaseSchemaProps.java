package com.booking.app.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("spring.datasource")
@Getter
@Setter
public class DatabaseSchemaProps {
    private String schema;
}
