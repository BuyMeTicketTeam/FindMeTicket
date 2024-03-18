package com.booking.app.mapper;

import com.booking.app.dto.EmailDTO;
import com.booking.app.dto.LoginDTO;
import com.booking.app.dto.RegistrationDTO;
import com.booking.app.dto.UserDTO;
import com.booking.app.entity.User;
import com.booking.app.entity.UserCredentials;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    public UserDTO toDtoUser(User user);

    public User toEntityUser(UserDTO userDTO);

    public LoginDTO toLoginDTO(UserCredentials userCredentials);

    public UserCredentials toUserSecurity(LoginDTO userDTO);

    public RegistrationDTO toRegistrationDTO(UserCredentials userCredentials);

    public UserCredentials toUserSecurity(RegistrationDTO registrationDTO);

    public EmailDTO toEmailDTO(UserCredentials userCredentials);

}