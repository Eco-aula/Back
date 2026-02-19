package com.java.ecoaula.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User buildUser(Integer id, String name, String email, String password) {
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        return user;
    }

    @Test
    void gettersSetters_andEqualsHashCode_work() {
        User u1 = buildUser(1, "David", "david@test.com", "1234");
        User u2 = buildUser(1, "David", "david@test.com", "1234");

        assertEquals(1, u1.getId());
        assertEquals("David", u1.getName());
        assertEquals("david@test.com", u1.getEmail());
        assertEquals("1234", u1.getPassword());

        assertEquals(u1, u2);
        assertEquals(u1.hashCode(), u2.hashCode());
        assertNotNull(u1.toString());
    }

    @Test
    void equals_whenSameReference_returnsTrue() {
        User user = buildUser(3, "Ana", "ana@test.com", "p");
        assertEquals(user, user);
    }

    @Test
    void equals_whenNull_returnsFalse() {
        User user = buildUser(3, "Ana", "ana@test.com", "p");
        assertNotEquals(user, null);
    }

    @Test
    void equals_whenDifferentType_returnsFalse() {
        User user = buildUser(3, "Ana", "ana@test.com", "p");
        assertNotEquals(user, "not-a-user");
    }

    @Test
    void equals_whenAnyFieldDiffers_returnsFalse() {
        User base = buildUser(3, "Ana", "ana@test.com", "p");
        assertNotEquals(base, buildUser(4, "Ana", "ana@test.com", "p"));
        assertNotEquals(base, buildUser(3, "B", "ana@test.com", "p"));
        assertNotEquals(base, buildUser(3, "Ana", "b@test.com", "p"));
        assertNotEquals(base, buildUser(3, "Ana", "ana@test.com", "x"));
    }
}
