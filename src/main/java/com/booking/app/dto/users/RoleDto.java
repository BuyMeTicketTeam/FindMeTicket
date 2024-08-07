package com.booking.app.dto.users;

import com.booking.app.constants.RoleType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class RoleDto {

    private final int id;

    @NotNull
    private final RoleType type;

}
