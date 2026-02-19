package com.java.ecoaula.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void getters_and_setters_work() {
        User user = new User();

        user.setId(1);
        user.setName("David");
        user.setEmail("david@ecoaula.com");
        user.setPassword("secret");

        assertEquals(1, user.getId());
        assertEquals("David", user.getName());
        assertEquals("david@ecoaula.com", user.getEmail());
        assertEquals("secret", user.getPassword());
    }

    @Test
    void equals_and_hashCode_work() {
        User u1 = new User();
        u1.setId(1);
        u1.setName("David");
        u1.setEmail("david@ecoaula.com");
        u1.setPassword("secret");

        User u2 = new User();
        u2.setId(1);
        u2.setName("David");
        u2.setEmail("david@ecoaula.com");
        u2.setPassword("secret");

        assertEquals(u1, u2);
        assertEquals(u1.hashCode(), u2.hashCode());
    }

    @Test
    void toString_contains_basic_fields() {
        User user = new User();
        user.setId(1);
        user.setName("David");
        user.setEmail("david@ecoaula.com");
        user.setPassword("secret");

        String text = user.toString();

        assertNotNull(text);
        assertTrue(text.contains("User"));
        assertTrue(text.contains("id=1"));
        assertTrue(text.contains("name=David"));
        assertTrue(text.contains("email=david@ecoaula.com"));
    }
}
