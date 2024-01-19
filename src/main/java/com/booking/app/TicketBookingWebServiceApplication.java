package com.booking.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TicketBookingWebServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TicketBookingWebServiceApplication.class, args);
	}


}


