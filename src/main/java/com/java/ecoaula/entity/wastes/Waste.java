package com.java.ecoaula.entity.wastes;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Waste{
    private Integer id;
    private String name;
    private Boolean organic;//true organico
    private String description;
    private float heavy;
}