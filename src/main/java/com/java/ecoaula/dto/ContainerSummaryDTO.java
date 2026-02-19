package com.java.ecoaula.dto;

import com.java.ecoaula.entity.Category;
import com.java.ecoaula.entity.State;

public class ContainerSummaryDTO {
     private Category category;
    private State state;

    public ContainerSummaryDTO(Category category, State state) {
        this.category = category;
        this.state = state;
    }

    public Category getCategory() {
        return category;
    }

    public State getState() {
        return state;
    }
}
