package com.java.ecoaula.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StateTest {

    @Test
    void values_contains_expected_constants() {
        State[] values = State.values();

        assertTrue(values.length >= 4);
        assertEquals(State.EMPTY, values[0]);
        assertEquals(State.LIMIT, values[1]);
        assertEquals(State.FULL, values[2]);
        assertEquals(State.RECYCLING, values[3]);
    }

    @Test
    void valueOf_works() {
        assertEquals(State.EMPTY, State.valueOf("EMPTY"));
        assertEquals(State.RECYCLING, State.valueOf("RECYCLING"));
    }

    @Test
    void name_and_ordinal_work() {
        assertEquals("EMPTY", State.EMPTY.name());
        assertEquals(0, State.EMPTY.ordinal());
    }
}
