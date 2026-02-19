package com.java.ecoaula.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class WasteTest {

    @Test
    void getters_and_setters_work() {
        Waste waste = new Waste();

        waste.setId(1);
        waste.setName("Botella");
        waste.setDescription("Botella de plástico");
        waste.setHeavy(0.25f);

        assertEquals(1, waste.getId());
        assertEquals("Botella", waste.getName());
        assertEquals("Botella de plástico", waste.getDescription());
        assertEquals(0.25f, waste.getHeavy(), 0.0001f);
    }

    @Test
    void equals_and_hashCode_work() {
        Waste w1 = new Waste();
        w1.setId(1);
        w1.setName("Botella");
        w1.setDescription("Botella de plástico");
        w1.setHeavy(0.25f);

        Waste w2 = new Waste();
        w2.setId(1);
        w2.setName("Botella");
        w2.setDescription("Botella de plástico");
        w2.setHeavy(0.25f);

        assertEquals(w1, w2);
        assertEquals(w1.hashCode(), w2.hashCode());
    }

    @Test
    void onCreate_sets_date_when_null() {
        Waste waste = new Waste();

        assertNull(waste.getDate());

        LocalDate today = LocalDate.now();
        waste.onCreate();

        assertNotNull(waste.getDate());
        assertEquals(today, waste.getDate());
    }
}
