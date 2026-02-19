package com.java.ecoaula.dto;

import com.java.ecoaula.entity.Category;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryVolumeDTOTest {

    @Test
    void constructor_whenTotalWeightNull_setsZero() {
        CategoryVolumeDTO dto = new CategoryVolumeDTO(Category.GLASS, null);
        assertEquals(Category.GLASS, dto.getCategory());
        assertEquals(0f, dto.getTotalWeight(), 0.0001f);
    }

    @Test
    void gettersSetters_work() {
        CategoryVolumeDTO dto = new CategoryVolumeDTO(Category.PLASTIC, 10.0);
        assertEquals(Category.PLASTIC, dto.getCategory());
        assertEquals(10f, dto.getTotalWeight(), 0.0001f);

        dto.setCategory(Category.METAL);
        dto.setTotalWeight(25.5f);

        assertEquals(Category.METAL, dto.getCategory());
        assertEquals(25.5f, dto.getTotalWeight(), 0.0001f);
    }
}
