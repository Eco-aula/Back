package com.java.ecoaula.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "containers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Container {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private State state;

    private float fillPercentage;

    @Enumerated(EnumType.STRING)
    private Category allowedCategory;


   @OneToMany(mappedBy = "container", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Waste> wastes;



    public void updateFillPercentage() {
    if (wastes == null || wastes.isEmpty()) {
        this.fillPercentage = 0;
    } else {
        float totalWeight = wastes.stream().map(Waste::getHeavy).reduce(0f, Float::sum);
        float maxCapacity = 100f;
        this.fillPercentage = (totalWeight / maxCapacity) * 100;
    }
    updateState();
}

public void updateState() {
    if (fillPercentage < 50) {
        this.state = State.EMPTY;
    } else if (fillPercentage < 90) {
        this.state = State.LIMIT;
    } else {
        this.state = State.FULL;
    }
}

public Category getAllowedCategory() {
    return allowedCategory;
}
}
