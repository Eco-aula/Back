/*package com.java.ecoaula.service;

import com.java.ecoaula.entity.User;
import com.java.ecoaula.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

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
                () -> userService.createUser(null)
        );

        assertEquals("El usuario no puede estar vacío", ex.getMessage());
        verifyNoInteractions(userRepository);
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
        assertEquals("1234", result.getPassword());

        verify(userRepository, times(1)).save(user);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void getUserById_whenIdIsNull_throwsIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> userService.getUserById(null)
        );

        assertEquals("El usuario no ha sido encontrado", ex.getMessage());
        verifyNoInteractions(userRepository);
    }

    @Test
    void getUserById_whenUserExists_returnsUser() {
        User user = new User();
        user.setId(1);
        user.setName("David");
        user.setEmail("david@test.com");

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        User result = userService.getUserById(1);

        assertNotNull(result);
        assertEquals("David", result.getName());
        assertEquals("david@test.com", result.getEmail());

        verify(userRepository, times(1)).findById(1);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void getUserById_whenUserNotFound_throwsRuntimeException() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> userService.getUserById(1)
        );

        assertEquals("Usuario no encontrado", ex.getMessage());
        verify(userRepository, times(1)).findById(1);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void deleteUser_whenIdIsNull_throwsIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> userService.deleteUser(null)
        );

        assertEquals("El usuario no existe", ex.getMessage());
        verifyNoInteractions(userRepository);
    }

    @Test
    void deleteUser_whenUserExists_deletesUser() {
        User user = new User();
        user.setId(1);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        userService.deleteUser(1);

        verify(userRepository, times(1)).findById(1);
        verify(userRepository, times(1)).delete(user);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void deleteUser_whenUserNotFound_throwsRuntimeException() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> userService.deleteUser(1)
        );

        assertEquals("No existe este usuario", ex.getMessage());
        verify(userRepository, times(1)).findById(1);
        verify(userRepository, never()).delete(any());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void updateUser_whenIdIsNull_throwsIllegalArgumentException() {
        User user = new User();

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> userService.updateUser(null, user)
        );

        assertEquals("El usuario no existe", ex.getMessage());
        verifyNoInteractions(userRepository);
    }

    @Test
    void updateUser_whenUserIsNull_throwsIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> userService.updateUser(1, null)
        );

        assertEquals("Los campos del usuario no pueden estar vacíos", ex.getMessage());
        verifyNoInteractions(userRepository);
    }

    @Test
    void updateUser_whenUserNotFound_throwsRuntimeException() {
        User updated = new User();
        updated.setName("New");
        updated.setEmail("new@test.com");
        updated.setPassword("456");

        when(userRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> userService.updateUser(1, updated)
        );

        assertEquals("El usuario no existe en la base de datos", ex.getMessage());
        verify(userRepository, times(1)).findById(1);
        verify(userRepository, never()).save(any());
        verifyNoMoreInteractions(userRepository);
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

        when(userRepository.findById(1)).thenReturn(Optional.of(existing));
        when(userRepository.save(existing)).thenReturn(existing);

        User result = userService.updateUser(1, updated);

        assertNotNull(result);
        assertEquals("New", result.getName());
        assertEquals("new@test.com", result.getEmail());
        assertEquals("456", result.getPassword());

        verify(userRepository, times(1)).findById(1);
        verify(userRepository, times(1)).save(existing);
        verifyNoMoreInteractions(userRepository);
    }
}*/
