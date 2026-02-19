/*package com.java.ecoaula.service;

import com.java.ecoaula.entity.Container;
import com.java.ecoaula.entity.State;
import com.java.ecoaula.entity.User;
import com.java.ecoaula.exception.ContainerNotFoundException;
import com.java.ecoaula.repository.ContainerRepository;
import com.java.ecoaula.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContainerServiceImplTest {

    @Mock
    ContainerRepository containerRepo;

    @Mock
    EmailService emailService;

    @Mock
    UserRepository userRepo;

    @InjectMocks
    ContainerServiceImpl containerService;

    @Test
    void updateFillPercentage_whenContainerNotFound_throwsRuntimeException() {
        when(containerRepo.findById(1)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> containerService.updateFillPercentage(1, 10f));
        assertEquals("Container no encontrado", ex.getMessage());

        verify(containerRepo, times(1)).findById(1);
        verifyNoMoreInteractions(containerRepo);
        verifyNoInteractions(emailService, userRepo);
    }

    @Test
    void updateFillPercentage_whenStateDoesNotChange_doesNotSendEmail() {
        Container container = new Container();
        container.setId(1);
        container.setState(State.EMPTY);
        container.setFillPercentage(10f);

        when(containerRepo.findById(1)).thenReturn(Optional.of(container));
        when(containerRepo.save(container)).thenReturn(container);

        containerService.updateFillPercentage(1, 10f);

        assertEquals(State.EMPTY, container.getState());
        verify(containerRepo, times(1)).findById(1);
        verify(containerRepo, times(1)).save(container);
        verifyNoInteractions(emailService);
        verifyNoInteractions(userRepo);
        verifyNoMoreInteractions(containerRepo);
    }

    @Test
    void updateFillPercentage_whenStateChangesToLimit_sendsEmailToAllUsers() {
        Container container = new Container();
        container.setId(1);
        container.setState(State.EMPTY);

        when(containerRepo.findById(1)).thenReturn(Optional.of(container));
        when(containerRepo.save(container)).thenReturn(container);

        User u1 = new User();
        u1.setEmail("a@test.com");
        User u2 = new User();
        u2.setEmail("b@test.com");

        when(userRepo.findAll()).thenReturn(List.of(u1, u2));

        containerService.updateFillPercentage(1, 70f);

        assertEquals(State.LIMIT, container.getState());
        verify(containerRepo, times(1)).findById(1);
        verify(containerRepo, times(1)).save(container);
        verify(userRepo, times(1)).findAll();
        verify(emailService, times(2)).send(anyString(), contains("70%"));
        verifyNoMoreInteractions(containerRepo, userRepo, emailService);
    }

    @Test
    void updateFillPercentage_whenStateChangesToFull_sendsEmailToAllUsers() {
        Container container = new Container();
        container.setId(1);
        container.setState(State.LIMIT);

        when(containerRepo.findById(1)).thenReturn(Optional.of(container));
        when(containerRepo.save(container)).thenReturn(container);

        User u1 = new User();
        u1.setEmail("a@test.com");
        User u2 = new User();
        u2.setEmail("b@test.com");

        when(userRepo.findAll()).thenReturn(List.of(u1, u2));

        containerService.updateFillPercentage(1, 95f);

        assertEquals(State.FULL, container.getState());
        verify(containerRepo, times(1)).findById(1);
        verify(containerRepo, times(1)).save(container);
        verify(userRepo, times(1)).findAll();
        verify(emailService, times(2)).send(anyString(), contains("URGENTE"));
        verifyNoMoreInteractions(containerRepo, userRepo, emailService);
    }

    @Test
    void setRecycling_whenBelow70_throwsIllegalStateException() {
        Container container = new Container();
        container.setId(1);
        container.setFillPercentage(69f);

        when(containerRepo.findById(1)).thenReturn(Optional.of(container));

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> containerService.setRecycling(1));
        assertEquals("No se puede reciclar si está por debajo del 70%", ex.getMessage());

        verify(containerRepo, times(1)).findById(1);
        verifyNoMoreInteractions(containerRepo);
        verifyNoInteractions(emailService, userRepo);
    }

    @Test
    void setRecycling_whenAtLeast70_setsStateRecyclingAndSaves() {
        Container container = new Container();
        container.setId(1);
        container.setFillPercentage(70f);

        when(containerRepo.findById(1)).thenReturn(Optional.of(container));
        when(containerRepo.save(container)).thenReturn(container);

        containerService.setRecycling(1);

        assertEquals(State.RECYCLING, container.getState());
        verify(containerRepo, times(1)).findById(1);
        verify(containerRepo, times(1)).save(container);
        verifyNoMoreInteractions(containerRepo);
        verifyNoInteractions(emailService, userRepo);
    }

    @Test
    void setRecycling_whenContainerNotFound_throwsRuntimeException() {
        when(containerRepo.findById(9)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> containerService.setRecycling(9));
        assertEquals("Container no encontrado", ex.getMessage());

        verify(containerRepo, times(1)).findById(9);
        verifyNoMoreInteractions(containerRepo);
        verifyNoInteractions(emailService, userRepo);
    }

    @Test
    void getById_whenExists_returnsContainer() {
        Container container = new Container();
        container.setId(5);

        when(containerRepo.findById(5)).thenReturn(Optional.of(container));

        Container result = containerService.getById(5);

        assertEquals(container, result);
        verify(containerRepo, times(1)).findById(5);
        verifyNoMoreInteractions(containerRepo);
        verifyNoInteractions(emailService, userRepo);
    }

    @Test
    void getById_whenNotFound_throwsContainerNotFoundException() {
        when(containerRepo.findById(12)).thenReturn(Optional.empty());

        ContainerNotFoundException ex = assertThrows(
                ContainerNotFoundException.class,
                () -> containerService.getById(12)
        );
        assertEquals("Container con id 12 no encontrado", ex.getMessage());

        verify(containerRepo, times(1)).findById(12);
        verifyNoMoreInteractions(containerRepo);
        verifyNoInteractions(emailService, userRepo);
    }
}*/
