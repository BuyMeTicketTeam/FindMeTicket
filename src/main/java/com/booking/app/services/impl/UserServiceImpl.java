package com.booking.app.services.impl;

import com.booking.app.dto.UserDTO;
import com.booking.app.entity.User;
import com.booking.app.exception.exception.ResourceNotFoundException;
import com.booking.app.mapper.UserMapper;
import com.booking.app.repositories.UserRepository;
import com.booking.app.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserMapper mapper;
    private final UserRepository repository;


    @Autowired
    public UserServiceImpl(UserMapper userMapper, UserRepository repository) {
        this.mapper = userMapper;
        this.repository = repository;
    }


    @Override
    public UserDTO create(UserDTO userDTO) {
        return mapper.toDtoUser(repository.save(mapper.toEntityUser(userDTO)));
    }

    @Override
    public List<UserDTO> getAll() {
        return repository.findAll().stream().map(mapper::toDtoUser).collect(Collectors.toList());
    }

    @Override
    public UserDTO getById(UUID id) {
        User user = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No user by ID: " + id));
        return mapper.toDtoUser(user);
    }

    @Override
    public UserDTO update(UUID id, UserDTO updated) {
        User user = mapper.toEntityUser(updated);
        user.setId(id);
        return mapper.toDtoUser(repository.save(user));
    }

    @Override
    public void delete(UUID id) {
        repository.deleteUserById(id);
    }





}