package com.booking.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
@EnableScheduling
@ConfigurationPropertiesScan(basePackages = {"com.booking.app.config"})
public class TicketBookingWebServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TicketBookingWebServiceApplication.class, args);
    }

}
