package com.booking.app.controller;

import com.booking.app.controller.api.UserAPI;
import com.booking.app.dto.ResponseDTO;
import com.booking.app.dto.UserDTO;
import com.booking.app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/users")
public class UserController implements UserAPI {

    private final UserService service;

    @Autowired
    public UserController(@Qualifier("userServiceImpl") UserService service) {
        this.service = service;
    }

    @GetMapping("/get/{id}")
    @Override
    public ResponseDTO<UserDTO> getByID(@PathVariable UUID id) {
        return new ResponseDTO<UserDTO>(service.getById(id));
    }

    @GetMapping("/get")
    @Override
    public ResponseDTO<List<UserDTO>> getAll() {
        return new ResponseDTO<List<UserDTO>>(service.getAll());
    }

    @PutMapping("/put/{id}")
    @Override
    public ResponseDTO<UserDTO> update(@PathVariable UUID id, @RequestBody UserDTO user) {
        return new ResponseDTO<UserDTO>(service.update(id, user));
    }

    @PostMapping("/create")
    @Override
    public ResponseDTO<UserDTO> create(@RequestBody UserDTO userDTO) {
        return new ResponseDTO<UserDTO>(service.create(userDTO));
    }

    @DeleteMapping("/delete/{id}")
    @Override
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}
