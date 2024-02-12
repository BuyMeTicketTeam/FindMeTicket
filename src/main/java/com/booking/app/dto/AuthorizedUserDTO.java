package com.booking.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthorizedUserDTO {
    private String username;

    private String basicPicture;

    private String googlePicture;
}
