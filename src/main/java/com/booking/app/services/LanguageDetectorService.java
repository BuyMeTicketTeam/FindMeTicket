package com.booking.app.services;

import java.io.IOException;
import java.util.Optional;

public interface LanguageDetectorService {
    Optional<String> detectLanguage(String letters) throws IOException;
}
