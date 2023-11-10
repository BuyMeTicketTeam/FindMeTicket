package com.booking.app.exception;

import lombok.*;
import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDetails {
    private LocalDateTime timeStamp;
    private String message;
    private String path;
    private HttpStatus status;
}
