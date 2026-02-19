package com.java.ecoaula.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class WasteTest {

    @Test
    void allArgsConstructor_setsFields() {
        Container container = new Container();
        LocalDate date = LocalDate.of(2026, 2, 1);

        Waste waste = new Waste(3, "Botella", "Vidrio", 2.5f, Category.GLASS, date, container);

        assertEquals(3, waste.getId());
        assertEquals("Botella", waste.getName());
        assertEquals("Vidrio", waste.getDescription());
        assertEquals(2.5f, waste.getHeavy(), 0.0001f);
        assertEquals(Category.GLASS, waste.getCategory());
        assertEquals(date, waste.getDate());
        assertEquals(container, waste.getContainer());
    }

    @Test
    void onCreate_setsDate_whenNull() {
        Waste w = new Waste();
        LocalDate before = LocalDate.now();
        w.onCreate();
        LocalDate after = LocalDate.now();

        assertNotNull(w.getDate());
        assertTrue(!w.getDate().isBefore(before));
        assertTrue(!w.getDate().isAfter(after));
    }

    @Test
    void onCreate_whenContainerPresent_setsCategoryFromContainer_andUpdatesContainerFill() {
        Container c = new Container();
        c.setAllowedCategory(Category.PLASTIC);
        ArrayList<Waste> wastes = new ArrayList<>();
        c.setWastes(wastes);
        c.setFillPercentage(0f);
        c.setState(State.EMPTY);

        Waste w = new Waste();
        w.setHeavy(40f);
        w.setContainer(c);
        wastes.add(w);

        w.onCreate();

        assertNotNull(w.getDate());
        assertEquals(Category.PLASTIC, w.getCategory());
        assertEquals(40f, c.getFillPercentage(), 0.0001f);
        assertEquals(State.EMPTY, c.getState());
    }
}
