package com.booking.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Arrays;


@SpringBootApplication
@EnableJpaAuditing
public class TicketBookingWebServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(TicketBookingWebServiceApplication.class, args);
	}




}


