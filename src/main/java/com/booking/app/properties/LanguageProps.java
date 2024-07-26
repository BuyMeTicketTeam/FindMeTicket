package com.booking.app.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties("languages.files")
@Setter
@Getter
public class LanguageProps {
    private List<String> profiles;
}
