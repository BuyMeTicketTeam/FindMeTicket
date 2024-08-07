package com.booking.app.configurations;

import com.booking.app.constants.DatePatternsConstants;
import com.booking.app.deserializers.CustomLocalDateDeserializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Configuration
@Slf4j
public class JacksonConfig {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DatePatternsConstants.DD_MM_YYYY_PATTERN);

    @Bean
    public ObjectMapper objectMapper() {
        log.info("Initializing ObjectMapper");

        ObjectMapper mapper = new ObjectMapper();

        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        log.info("Configured ObjectMapper to ignore unknown properties");

        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        log.info("Configured ObjectMapper to exclude null values during serialization");

        mapper.registerModule(initTimeModule());
        log.info("Configured ObjectMapper to use {} date format", DatePatternsConstants.DD_MM_YYYY_PATTERN);

        return mapper;
    }

    private SimpleModule initTimeModule() {
        return new JavaTimeModule()
                .addSerializer(LocalDate.class, new LocalDateSerializer(formatter))
                .addDeserializer(LocalDate.class, new CustomLocalDateDeserializer());
    }

}
