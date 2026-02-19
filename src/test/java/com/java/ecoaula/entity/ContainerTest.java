package com.java.ecoaula.entity;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ContainerTest {

    @Test
    void allArgsConstructor_setsAllFields() {
        Waste waste = new Waste();
        waste.setHeavy(12f);

        Container c = new Container(5, State.LIMIT, 12f, Category.METAL, List.of(waste));

        assertEquals(5, c.getId());
        assertEquals(State.LIMIT, c.getState());
        assertEquals(12f, c.getFillPercentage(), 0.0001f);
        assertEquals(Category.METAL, c.getAllowedCategory());
        assertEquals(1, c.getWastes().size());
    }

    @Test
    void updateFillPercentage_whenWastesNull_setsFillToZero_andStateEmpty() {
        Container c = new Container();
        c.setWastes(null);
        c.updateFillPercentage();

        assertEquals(0f, c.getFillPercentage(), 0.0001f);
        assertEquals(State.EMPTY, c.getState());
    }

    @Test
    void updateFillPercentage_whenWastesEmpty_setsFillToZero_andStateEmpty() {
        Container c = new Container();
        c.setWastes(new ArrayList<>());
        c.updateFillPercentage();

        assertEquals(0f, c.getFillPercentage(), 0.0001f);
        assertEquals(State.EMPTY, c.getState());
    }

    @Test
    void updateFillPercentage_whenTotalWeight30_setsFill30_andStateEmpty() {
        Waste w1 = new Waste();
        w1.setHeavy(10f);
        Waste w2 = new Waste();
        w2.setHeavy(20f);

        Container c = new Container();
        c.setWastes(List.of(w1, w2));
        c.updateFillPercentage();

        assertEquals(30f, c.getFillPercentage(), 0.0001f);
        assertEquals(State.EMPTY, c.getState());
    }

    @Test
    void updateFillPercentage_whenTotalWeight70_setsFill70_andStateLimit() {
        Waste w1 = new Waste();
        w1.setHeavy(50f);
        Waste w2 = new Waste();
        w2.setHeavy(20f);

        Container c = new Container();
        c.setWastes(List.of(w1, w2));
        c.updateFillPercentage();

        assertEquals(70f, c.getFillPercentage(), 0.0001f);
        assertEquals(State.LIMIT, c.getState());
    }

    @Test
    void updateFillPercentage_whenTotalWeight90_setsFill90_andStateFull() {
        Waste w1 = new Waste();
        w1.setHeavy(90f);

        Container c = new Container();
        c.setWastes(List.of(w1));
        c.updateFillPercentage();

        assertEquals(90f, c.getFillPercentage(), 0.0001f);
        assertEquals(State.FULL, c.getState());
    }

    @Test
    void updateState_boundaries() {
        Container c = new Container();

        c.setFillPercentage(0f);
        c.updateState();
        assertEquals(State.EMPTY, c.getState());

        c.setFillPercentage(49.999f);
        c.updateState();
        assertEquals(State.EMPTY, c.getState());

        c.setFillPercentage(50f);
        c.updateState();
        assertEquals(State.LIMIT, c.getState());

        c.setFillPercentage(89.999f);
        c.updateState();
        assertEquals(State.LIMIT, c.getState());

        c.setFillPercentage(90f);
        c.updateState();
        assertEquals(State.FULL, c.getState());
    }

    @Test
    void getAllowedCategory_returnsValue() {
        Container c = new Container();
        c.setAllowedCategory(Category.GLASS);
        assertEquals(Category.GLASS, c.getAllowedCategory());
    }
}
