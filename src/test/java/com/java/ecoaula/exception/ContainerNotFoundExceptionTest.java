package com.java.ecoaula.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ContainerNotFoundExceptionTest {

    @Test
    void constructor_setsExpectedMessage() {
        ContainerNotFoundException ex = new ContainerNotFoundException(42);
        assertEquals("Container con id 42 no encontrado", ex.getMessage());
    }
}
