package com.java.ecoaula.service;

import com.java.ecoaula.entity.User;
import com.java.ecoaula.exception.ResourceNotFoundException;
import com.java.ecoaula.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("El usuario no puede estar vacio");
        }
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Integer id, User updUser) {
        if (id == null) {
            throw new IllegalArgumentException("El usuario no existe");
        }
        if (updUser == null) {
            throw new IllegalArgumentException("Los campos del usuario no pueden estar vacios");
        }

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El usuario no existe en la base de datos"));

        existingUser.setName(updUser.getName());
        existingUser.setEmail(updUser.getEmail());
        existingUser.setPassword(updUser.getPassword());
        return userRepository.save(existingUser);
    }

    @Override
    public void deleteUser(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("El usuario no existe");
        }

        userRepository.findById(id).ifPresentOrElse(
                userRepository::delete,
                () -> {
                    throw new ResourceNotFoundException("No existe este usuario");
                }
        );
    }

    @Override
    public User getUserById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("El usuario no ha sido encontrado");
        }
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
    }
}
