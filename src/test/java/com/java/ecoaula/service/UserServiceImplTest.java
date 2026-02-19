package com.java.ecoaula.service;

import com.java.ecoaula.entity.User;
import com.java.ecoaula.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserServiceImpl userService;

    @Test
    void createUser_whenUserIsNull_throwsIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> userService.createUser(null));

        assertEquals("El usuario no puede estar vacío", ex.getMessage());
    }

    @Test
    void createUser_whenValidUser_returnsSavedUser() {
        User user = new User();
        user.setName("David");
        user.setEmail("david@test.com");
        user.setPassword("1234");

        when(userRepository.save(user)).thenReturn(user);

        User result = userService.createUser(user);

        assertNotNull(result);
        assertEquals("David", result.getName());
        assertEquals("david@test.com", result.getEmail());

        verify(userRepository, times(1)).save(user);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void getUserById_whenUserExists_returnsUser() {
        User user = new User();
        user.setId(1);
        user.setName("David");
        user.setEmail("david@test.com");

        when(userRepository.findById(1)).thenReturn(java.util.Optional.of(user));

        User result = userService.getUserById(1);

        assertNotNull(result);
        assertEquals("David", result.getName());
        verify(userRepository).findById(1);
    }

    @Test
    void getUserById_whenUserNotFound_throwsRuntimeException() {
        when(userRepository.findById(1)).thenReturn(java.util.Optional.empty());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> userService.getUserById(1));

        assertEquals("Usuario no encontrado", ex.getMessage());
    }

    @Test
    void deleteUser_whenIdIsNull_throwsIllegalArgumentException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> userService.deleteUser(null));
    }

    @Test
    void deleteUser_whenUserExists_deletesUser() {
        User user = new User();
        user.setId(1);

        when(userRepository.findById(1)).thenReturn(java.util.Optional.of(user));

        userService.deleteUser(1);

        verify(userRepository).delete(user);
    }

    @Test
    void updateUser_whenIdIsNull_throwsIllegalArgumentException() {
        User user = new User();
        assertThrows(
                IllegalArgumentException.class,
                () -> userService.updateUser(null, user));
    }

    @Test
    void updateUser_whenValid_updatesAndReturnsUser() {
        User existing = new User();
        existing.setId(1);
        existing.setName("Old");
        existing.setEmail("old@test.com");
        existing.setPassword("123");

        User updated = new User();
        updated.setName("New");
        updated.setEmail("new@test.com");
        updated.setPassword("456");

        when(userRepository.findById(1)).thenReturn(java.util.Optional.of(existing));
        when(userRepository.save(existing)).thenReturn(existing);

        User result = userService.updateUser(1, updated);

        assertEquals("New", result.getName());
        assertEquals("new@test.com", result.getEmail());
        verify(userRepository).save(existing);
    }
}
