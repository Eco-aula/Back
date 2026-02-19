package com.java.ecoaula.dto;

import java.util.Locale.Category;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VolumeByCategoryDTO {
    private Category category;
    private Double totalVolume;
}
