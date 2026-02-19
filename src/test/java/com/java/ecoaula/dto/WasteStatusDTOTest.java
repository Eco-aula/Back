package com.java.ecoaula.dto;

import com.java.ecoaula.entity.Category;
import com.java.ecoaula.entity.State;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WasteStatusDTOTest {

    @Test
    void record_accessors_work() {
        WasteStatusDTO dto = new WasteStatusDTO(
                10,
                "Botella",
                Category.PLASTIC,
                12.5f,
                State.LIMIT,
                "Aula 1"
        );

        assertEquals(10, dto.wasteId());
        assertEquals("Botella", dto.name());
        assertEquals(Category.PLASTIC, dto.category());
        assertEquals(12.5f, dto.heavy(), 0.0001f);
        assertEquals(State.LIMIT, dto.containerState());
        assertEquals("Aula 1", dto.containerLocation());
    }
}
