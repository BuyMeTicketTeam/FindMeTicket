package com.booking.app.dto;

import com.booking.app.enums.EnumRole;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class RoleDTO {

    private final int id;
    @NotNull

    private final EnumRole enumRole;

}
