package com.booking.app.services;

import com.booking.app.dto.UserDTO;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

public interface UserService {

    @Transactional
    UserDTO create(UserDTO user);

    List<UserDTO> getAll();

    UserDTO getById(UUID id);

    @Transactional
    UserDTO update(UUID id, UserDTO user);

    @Transactional
    void delete(UUID id);
}
