package com.java.ecoaula.service;

import org.springframework.stereotype.Service;

import com.java.ecoaula.entity.User;

@Service
public interface UserService {
    public User createUser(User user);

    public User updateUser(Integer id, User updUser);

    public void deleteUser(Integer id);

    public User getUserById(Integer id);
}
