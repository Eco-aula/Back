package com.java.ecoaula.dto;

import com.java.ecoaula.entity.Category;
import com.java.ecoaula.entity.State;


public record WasteStatusDTO(
    Integer wasteId,
    String name,
    Category category,
    float heavy,
    State containerState,
    String containerLocation
) {}
