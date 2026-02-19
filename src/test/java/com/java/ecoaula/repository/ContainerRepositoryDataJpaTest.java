/*package com.java.ecoaula.repository;

import com.java.ecoaula.dto.CategoryVolumeDTO;
import com.java.ecoaula.entity.Category;
import com.java.ecoaula.entity.Container;
import com.java.ecoaula.entity.State;
import com.java.ecoaula.entity.Waste;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class ContainerRepositoryDataJpaTest {

    @Autowired
    ContainerRepository containerRepository;

    @Autowired
    WasteRepository wasteRepository;

    @Test
    void findByAllowedCategory_returnsContainerWhenExists() {
        Container container = new Container();
        container.setAllowedCategory(Category.GLASS);
        container.setFillPercentage(0f);
        container.setState(State.EMPTY);
        container.setWastes(new ArrayList<>());
        Container saved = containerRepository.save(container);

        assertTrue(containerRepository.findByAllowedCategory(Category.GLASS).isPresent());
        assertEquals(saved.getId(), containerRepository.findByAllowedCategory(Category.GLASS).orElseThrow().getId());
        assertTrue(containerRepository.findByAllowedCategory(Category.METAL).isEmpty());
    }

    @Test
    void getVolumeByCategory_returnsAggregatedTotals() {
        Container glassContainer = new Container();
        glassContainer.setAllowedCategory(Category.GLASS);
        glassContainer.setFillPercentage(0f);
        glassContainer.setState(State.EMPTY);
        glassContainer.setWastes(new ArrayList<>());

        Container paperContainer = new Container();
        paperContainer.setAllowedCategory(Category.PAPER);
        paperContainer.setFillPercentage(0f);
        paperContainer.setState(State.EMPTY);
        paperContainer.setWastes(new ArrayList<>());

        glassContainer = containerRepository.save(glassContainer);
        paperContainer = containerRepository.save(paperContainer);

        Waste w1 = new Waste();
        w1.setName("Botella 1");
        w1.setDescription("Vidrio");
        w1.setHeavy(10f);
        w1.setContainer(glassContainer);

        Waste w2 = new Waste();
        w2.setName("Botella 2");
        w2.setDescription("Vidrio");
        w2.setHeavy(5f);
        w2.setContainer(glassContainer);

        Waste w3 = new Waste();
        w3.setName("Papel");
        w3.setDescription("Carton");
        w3.setHeavy(7f);
        w3.setContainer(paperContainer);

        wasteRepository.saveAll(List.of(w1, w2, w3));

        List<CategoryVolumeDTO> totals = containerRepository.getVolumeByCategory();
        Map<Category, Float> byCategory = totals.stream()
                .collect(Collectors.toMap(CategoryVolumeDTO::getCategory, CategoryVolumeDTO::getTotalWeight));

        assertEquals(2, byCategory.size());
        assertEquals(15f, byCategory.get(Category.GLASS), 0.0001f);
        assertEquals(7f, byCategory.get(Category.PAPER), 0.0001f);
    }
}*/
