package com.booking.app.controller.dto;

import com.booking.app.enums.EnumRole;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
public class RoleDTO {
    private final int id;
    @NotNull
    private final EnumRole enumRole;

}
