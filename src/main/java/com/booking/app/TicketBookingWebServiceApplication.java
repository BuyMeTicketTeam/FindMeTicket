package com.booking.app;

import com.booking.app.config.LinkProps;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
@EnableScheduling
@EnableConfigurationProperties(LinkProps.class)
public class TicketBookingWebServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TicketBookingWebServiceApplication.class, args);
    }

}
