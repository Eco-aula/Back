package com.java.ecoaula.entity.wastes;

import com.java.ecoaula.type.WasteType;

import lombok.Data;

@Data
public class Waste{
    private int id;
    private String name;
    private WasteType type;
    private String description;
    private float weight;
}