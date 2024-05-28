package com.booking.app.mapper;

import com.booking.app.dto.EmailDTO;
import com.booking.app.dto.LoginDTO;
import com.booking.app.dto.RegistrationDTO;
import com.booking.app.dto.UserDTO;
import com.booking.app.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    UserDTO toUserDto(User user);

    User toUser(UserDTO userDTO);

    LoginDTO toLoginDto(User user);

    User toUser(LoginDTO userDTO);

    RegistrationDTO toRegistrationDto(User user);

    User toUser(RegistrationDTO registrationDTO);

    EmailDTO toEmailDto(User user);

}