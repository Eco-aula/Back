package com.java.ecoaula.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UpdateFillDTOTest {

    @Test
    void record_accessors_work() {
        UpdateFillDTO dto = new UpdateFillDTO(55.5f);
        assertEquals(55.5f, dto.percentage(), 0.0001f);
    }
}
