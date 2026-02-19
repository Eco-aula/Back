package com.java.ecoaula.service;

import com.java.ecoaula.entity.Category;
import com.java.ecoaula.entity.Container;
import com.java.ecoaula.entity.Waste;
import com.java.ecoaula.repository.ContainerRepository;
import com.java.ecoaula.repository.WasteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WasteServiceImplTest {

    @Mock
    WasteRepository wasteRepository;

    @Mock
    ContainerRepository containerRepository;

    @InjectMocks
    WasteServiceImpl service;

    @Test
    void createWaste_whenNull_throwsIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.createWaste(null));
        assertEquals("El residuo no puede estar vacío", ex.getMessage());
        verifyNoInteractions(wasteRepository, containerRepository);
    }

    @Test
    void createWaste_whenContainerExists_attachesWaste_updatesContainer_andSavesWaste() {
        Container existing = new Container();
        existing.setId(1);
        existing.setAllowedCategory(Category.GLASS);
        existing.setFillPercentage(0f);
        existing.setWastes(new ArrayList<>());

        Waste w = new Waste();
        w.setName("Botella");
        w.setHeavy(20f);
        w.setCategory(Category.GLASS);

        when(containerRepository.findByAllowedCategory(Category.GLASS)).thenReturn(Optional.of(existing));
        when(wasteRepository.save(any(Waste.class))).thenAnswer(inv -> inv.getArgument(0));

        Waste result = service.createWaste(w);

        assertNotNull(result);
        assertNotNull(result.getContainer());
        assertEquals(1, result.getContainer().getId());
        assertEquals(1, existing.getWastes().size());

        verify(containerRepository, times(1)).findByAllowedCategory(Category.GLASS);
        verify(containerRepository, never()).save(any(Container.class));
        verify(wasteRepository, times(1)).save(w);
        verifyNoMoreInteractions(wasteRepository, containerRepository);
    }

    @Test
    void createWaste_whenContainerDoesNotExist_createsContainer_andSavesBoth() {
        Waste w = new Waste();
        w.setName("Papel");
        w.setHeavy(10f);
        w.setCategory(Category.PAPER);

        when(containerRepository.findByAllowedCategory(Category.PAPER)).thenReturn(Optional.empty());

        when(containerRepository.save(any(Container.class))).thenAnswer(inv -> {
            Container c = inv.getArgument(0);
            c.setId(99);
            return c;
        });

        when(wasteRepository.save(any(Waste.class))).thenAnswer(inv -> inv.getArgument(0));

        Waste result = service.createWaste(w);

        assertNotNull(result);
        assertNotNull(result.getContainer());
        assertEquals(99, result.getContainer().getId());
        assertEquals(Category.PAPER, result.getContainer().getAllowedCategory());
        assertNotNull(result.getContainer().getWastes());
        assertEquals(1, result.getContainer().getWastes().size());

        verify(containerRepository, times(1)).findByAllowedCategory(Category.PAPER);
        verify(containerRepository, times(1)).save(any(Container.class));
        verify(wasteRepository, times(1)).save(w);
        verifyNoMoreInteractions(wasteRepository, containerRepository);
    }

    @Test
    void updateWaste_whenIdNull_throwsIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.updateWaste(null, new Waste()));
        assertEquals("El residuo no existe", ex.getMessage());
        verifyNoInteractions(wasteRepository, containerRepository);
    }

    @Test
    void updateWaste_whenBodyNull_throwsIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.updateWaste(1, null));
        assertEquals("El residuo no puede estar vacío", ex.getMessage());
        verifyNoInteractions(wasteRepository, containerRepository);
    }

    @Test
    void updateWaste_whenNotFound_throwsRuntimeException() {
        when(wasteRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.updateWaste(1, new Waste()));
        assertEquals("El residuo no existe en la base de datos", ex.getMessage());

        verify(wasteRepository, times(1)).findById(1);
        verifyNoMoreInteractions(wasteRepository);
        verifyNoInteractions(containerRepository);
    }

    @Test
    void updateWaste_whenFound_updatesFields_andSaves() {
        Waste existing = new Waste();
        existing.setId(1);
        existing.setName("Old");
        existing.setDescription("OldDesc");
        existing.setHeavy(1f);
        existing.setCategory(Category.PLASTIC);

        Waste upd = new Waste();
        upd.setName("New");
        upd.setDescription("NewDesc");
        upd.setHeavy(5f);
        upd.setCategory(Category.METAL);

        when(wasteRepository.findById(1)).thenReturn(Optional.of(existing));
        when(wasteRepository.save(any(Waste.class))).thenAnswer(inv -> inv.getArgument(0));

        Waste result = service.updateWaste(1, upd);

        assertNotNull(result);
        assertEquals("New", existing.getName());
        assertEquals("NewDesc", existing.getDescription());
        assertEquals(5f, existing.getHeavy(), 0.0001f);
        assertEquals(Category.METAL, existing.getCategory());

        verify(wasteRepository, times(1)).findById(1);
        verify(wasteRepository, times(1)).save(existing);
        verifyNoMoreInteractions(wasteRepository);
        verifyNoInteractions(containerRepository);
    }

    @Test
    void deleteWaste_whenIdNull_throwsIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.deleteWaste(null));
        assertEquals("El residuo no existe", ex.getMessage());
        verifyNoInteractions(wasteRepository, containerRepository);
    }

    @Test
    void deleteWaste_whenFound_deletes() {
        Waste existing = new Waste();
        existing.setId(1);

        when(wasteRepository.findById(1)).thenReturn(Optional.of(existing));

        service.deleteWaste(1);

        verify(wasteRepository, times(1)).findById(1);
        verify(wasteRepository, times(1)).delete(existing);
        verifyNoMoreInteractions(wasteRepository);
        verifyNoInteractions(containerRepository);
    }

    @Test
    void deleteWaste_whenNotFound_throwsRuntimeException() {
        when(wasteRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.deleteWaste(1));
        assertEquals("El residuo no existe", ex.getMessage());

        verify(wasteRepository, times(1)).findById(1);
        verify(wasteRepository, never()).delete(any());
        verifyNoMoreInteractions(wasteRepository);
        verifyNoInteractions(containerRepository);
    }
}
