package com.booking.app.mapper;

import com.booking.app.controller.dto.LoginDTO;
import com.booking.app.controller.dto.RegistrationDTO;
import com.booking.app.controller.dto.RoleDTO;
import com.booking.app.controller.dto.UserDTO;
import com.booking.app.entity.Role;
import com.booking.app.entity.User;
import com.booking.app.entity.UserSecurity;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-11-04T15:50:49+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 19.0.2 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDTO toDtoUser(User user) {
        if ( user == null ) {
            return null;
        }

        String phoneNumber = null;
        RoleDTO role = null;

        phoneNumber = user.getPhoneNumber();
        role = roleToRoleDTO( user.getRole() );

        UserDTO userDTO = new UserDTO( phoneNumber, role );

        return userDTO;
    }

    @Override
    public User toEntityUser(UserDTO userDTO) {
        if ( userDTO == null ) {
            return null;
        }

        User user = new User();

        user.setPhoneNumber( userDTO.getPhoneNumber() );
        user.setRole( roleDTOToRole( userDTO.getRole() ) );

        return user;
    }

    @Override
    public LoginDTO toDtoLogin(UserSecurity userSecurity) {
        if ( userSecurity == null ) {
            return null;
        }

        LoginDTO.LoginDTOBuilder loginDTO = LoginDTO.builder();

        loginDTO.email( userSecurity.getEmail() );
        loginDTO.password( userSecurity.getPassword() );
        loginDTO.user( toDtoUser( userSecurity.getUser() ) );

        return loginDTO.build();
    }

    @Override
    public UserSecurity toEntityLogin(LoginDTO userDTO) {
        if ( userDTO == null ) {
            return null;
        }

        UserSecurity.UserSecurityBuilder userSecurity = UserSecurity.builder();

        userSecurity.email( userDTO.getEmail() );
        userSecurity.password( userDTO.getPassword() );
        userSecurity.user( toEntityUser( userDTO.getUser() ) );

        return userSecurity.build();
    }

    @Override
    public RegistrationDTO toDtoRegistration(UserSecurity userSecurity) {
        if ( userSecurity == null ) {
            return null;
        }

        RegistrationDTO.RegistrationDTOBuilder registrationDTO = RegistrationDTO.builder();

        registrationDTO.username( userSecurity.getUsername() );
        registrationDTO.email( userSecurity.getEmail() );
        registrationDTO.password( userSecurity.getPassword() );
        registrationDTO.user( toDtoUser( userSecurity.getUser() ) );

        return registrationDTO.build();
    }

    @Override
    public UserSecurity toEntityRegistration(RegistrationDTO registrationDTO) {
        if ( registrationDTO == null ) {
            return null;
        }

        UserSecurity.UserSecurityBuilder userSecurity = UserSecurity.builder();

        userSecurity.email( registrationDTO.getEmail() );
        userSecurity.password( registrationDTO.getPassword() );
        userSecurity.username( registrationDTO.getUsername() );
        userSecurity.user( toEntityUser( registrationDTO.getUser() ) );

        return userSecurity.build();
    }

    protected RoleDTO roleToRoleDTO(Role role) {
        if ( role == null ) {
            return null;
        }

        RoleDTO.RoleDTOBuilder roleDTO = RoleDTO.builder();

        roleDTO.id( role.getId() );
        roleDTO.enumRole( role.getEnumRole() );

        return roleDTO.build();
    }

    protected Role roleDTOToRole(RoleDTO roleDTO) {
        if ( roleDTO == null ) {
            return null;
        }

        Role.RoleBuilder role = Role.builder();

        role.id( roleDTO.getId() );
        role.enumRole( roleDTO.getEnumRole() );

        return role.build();
    }
}
