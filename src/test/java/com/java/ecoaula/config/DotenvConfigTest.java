package com.java.ecoaula.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class DotenvConfigTest {

    @Test
    void class_loads_without_exception() throws ClassNotFoundException {
        Class<?> clazz = Class.forName("com.java.ecoaula.config.DotenvConfig");
        assertNotNull(clazz);
    }
}
