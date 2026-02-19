package com.java.ecoaula.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "wastes")
public class Waste {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String description;
    private float heavy;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(nullable = false, updatable = false)
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "container_id")
    @JsonIgnore
    private Container container;

    public Waste() {
    }

    public Waste(Integer id, String name, String description, float heavy, Category category, LocalDate date, Container container) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.heavy = heavy;
        this.category = category;
        this.date = date;
        this.container = container;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getHeavy() {
        return heavy;
    }

    public void setHeavy(float heavy) {
        this.heavy = heavy;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Container getContainer() {
        return container;
    }

    public void setContainer(Container container) {
        this.container = container;
    }

    @PrePersist
    public void onCreate() {
        this.date = LocalDate.now();
        if (container != null) {
            this.category = container.getAllowedCategory();
            container.updateFillPercentage();
        }
    }
}
