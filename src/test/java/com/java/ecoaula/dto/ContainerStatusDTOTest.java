package com.java.ecoaula.dto;

import com.java.ecoaula.entity.State;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContainerStatusDTOTest {

    @Test
    void record_accessors_work() {
        ContainerStatusDTO dto = new ContainerStatusDTO(1, 70f, State.LIMIT);
        assertEquals(1, dto.id());
        assertEquals(70f, dto.fillPercentage(), 0.0001f);
        assertEquals(State.LIMIT, dto.state());
    }
}
