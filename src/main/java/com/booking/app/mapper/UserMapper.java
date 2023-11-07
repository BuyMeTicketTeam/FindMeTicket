package com.booking.app.mapper;

import com.booking.app.controller.dto.LoginDTO;
import com.booking.app.controller.dto.RegistrationDTO;
import com.booking.app.controller.dto.UserDTO;
import com.booking.app.entity.User;
import com.booking.app.entity.UserSecurity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    public UserDTO toDtoUser(User user);

    public User toEntityUser(UserDTO userDTO);

    public LoginDTO toDtoLogin(UserSecurity userSecurity);

    public UserSecurity toEntityLogin(LoginDTO userDTO);

    public RegistrationDTO toDtoRegistration(UserSecurity userSecurity);

    public UserSecurity toEntityRegistration(RegistrationDTO registrationDTO);

}