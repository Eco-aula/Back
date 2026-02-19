package com.java.ecoaula.dto;

import com.java.ecoaula.entity.Category;

public class CategoryVolumeDTO {

    private Category category;
    private float totalWeight;

    public CategoryVolumeDTO(Category category, Double totalWeight) {
        this.category = category;
        this.totalWeight = totalWeight != null ? totalWeight.floatValue() : 0f;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public float getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(float totalWeight) {
        this.totalWeight = totalWeight;
    }
}

