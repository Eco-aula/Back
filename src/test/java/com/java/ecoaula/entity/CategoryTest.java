package com.java.ecoaula.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    @Test
    void values_contains_expected_constants() {
        Category[] values = Category.values();

        assertTrue(values.length >= 6);
        assertEquals(Category.PLASTIC, values[0]);
        assertEquals(Category.GLAS, values[1]);
        assertEquals(Category.CARDBOARD, values[2]);
        assertEquals(Category.ORGANIC, values[3]);
        assertEquals(Category.PAPER, values[4]);
        assertEquals(Category.METAL, values[5]);
    }

    @Test
    void valueOf_works() {
        assertEquals(Category.PLASTIC, Category.valueOf("PLASTIC"));
        assertEquals(Category.METAL, Category.valueOf("METAL"));
    }

    @Test
    void name_and_ordinal_work() {
        assertEquals("PLASTIC", Category.PLASTIC.name());
        assertEquals(0, Category.PLASTIC.ordinal());
    }
}
