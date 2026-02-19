package com.java.ecoaula.service;

import org.springframework.stereotype.Service;

import com.java.ecoaula.entity.User;

@Service
public interface UserService {
    User createUser(User user);

    User updateUser(Integer id, User updUser);

    void deleteUser(Integer id);

    User getUserById(Integer id);
}
