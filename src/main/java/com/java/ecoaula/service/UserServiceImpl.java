package com.java.ecoaula.service;

import org.springframework.stereotype.Service;

import com.java.ecoaula.entity.wastes.User;
import com.java.ecoaula.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository){
        this.userRepository=userRepository;
    }
    @Override
    public User createUser(User user) {
        if(user==null){
            throw new IllegalArgumentException("El usuario no puede estar vacío");
        }
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Integer id, User updUser) {
        if(id==null) throw new IllegalArgumentException("El usuario no existe");
        if(updUser==null) throw new IllegalArgumentException("Los campos del usuario no pueden estar vacíos");
        User existingUser=userRepository.findById(id).orElseThrow(()->new RuntimeException("El usuario no existe en la base de datos"));
        existingUser.setName(updUser.getName());
        existingUser.setEmail(updUser.getEmail());
        existingUser.setPassword(updUser.getPassword());
        User updatedUser=userRepository.save(existingUser);
        return updatedUser;
    }

    @Override
    public void deleteUser(Integer id) {
        if(id==null) throw new IllegalArgumentException("El usuario no existe");
        userRepository.findById(id).ifPresentOrElse(userRepository::delete,()->{
            throw new RuntimeException("No existe este usuario");
        } );
    }
    @Override
    public User getUserById(Integer id) {
        if(id==null) throw new IllegalArgumentException("El usuario no ha sido encontrado");
        return userRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    }
}
