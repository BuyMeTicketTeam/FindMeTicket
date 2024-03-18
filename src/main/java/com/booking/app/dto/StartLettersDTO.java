package com.booking.app.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
public record StartLettersDTO(@NotNull @Getter String startLetters){}
