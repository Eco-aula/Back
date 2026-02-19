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

@Entity
@Table(name = "containers")
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

    public Container() {
    }

    public Container(Integer id, State state, float fillPercentage, Category allowedCategory, List<Waste> wastes) {
        this.id = id;
        this.state = state;
        this.fillPercentage = fillPercentage;
        this.allowedCategory = allowedCategory;
        this.wastes = wastes;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public float getFillPercentage() {
        return fillPercentage;
    }

    public void setFillPercentage(float fillPercentage) {
        this.fillPercentage = fillPercentage;
    }

    public void setAllowedCategory(Category allowedCategory) {
        this.allowedCategory = allowedCategory;
    }

    public List<Waste> getWastes() {
        return wastes;
    }

    public void setWastes(List<Waste> wastes) {
        this.wastes = wastes;
    }

    public void updateFillPercentage() {
    if (wastes == null || wastes.isEmpty()) {
        this.fillPercentage = 0;
    } else {
        float totalWeight = (float) wastes.stream().mapToDouble(Waste::getHeavy).sum();
        float maxCapacity = 100f;
        this.fillPercentage = totalWeight / maxCapacity * 100;
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
