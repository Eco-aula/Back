package com.java.ecoaula.service;

import com.java.ecoaula.entity.Container;
import com.java.ecoaula.entity.Waste;
import com.java.ecoaula.exception.ResourceNotFoundException;
import com.java.ecoaula.repository.ContainerRepository;
import com.java.ecoaula.repository.WasteRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Transactional
public class WasteServiceImpl implements WasteService {
    private final WasteRepository wasteRepository;
    private final ContainerRepository containerRepository;

    public WasteServiceImpl(WasteRepository wasteRepository, ContainerRepository containerRepository) {
        this.wasteRepository = wasteRepository;
        this.containerRepository = containerRepository;
    }

    @Override
    public Waste createWaste(Waste waste) {
        if (waste == null) {
            throw new IllegalArgumentException("El residuo no puede estar vacio");
        }

        Container container = containerRepository.findByAllowedCategory(waste.getCategory())
                .orElseGet(() -> {
                    Container newContainer = new Container();
                    newContainer.setAllowedCategory(waste.getCategory());
                    newContainer.setFillPercentage(0.0f);
                    newContainer.setWastes(new ArrayList<>());
                    return containerRepository.save(newContainer);
                });

        waste.setContainer(container);
        container.getWastes().add(waste);
        container.updateFillPercentage();

        return wasteRepository.save(waste);
    }

    @Override
    public Waste updateWaste(Integer id, Waste upWaste) {
        if (id == null) {
            throw new IllegalArgumentException("El residuo no existe");
        }
        if (upWaste == null) {
            throw new IllegalArgumentException("El residuo no puede estar vacio");
        }

        Waste existingWaste = wasteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El residuo no existe en la base de datos"));

        existingWaste.setName(upWaste.getName());
        existingWaste.setDescription(upWaste.getDescription());
        existingWaste.setHeavy(upWaste.getHeavy());
        existingWaste.setCategory(upWaste.getCategory());
        return wasteRepository.save(existingWaste);
    }

    @Override
    public void deleteWaste(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("El residuo no existe");
        }

        wasteRepository.findById(id).ifPresentOrElse(
                wasteRepository::delete,
                () -> {
                    throw new ResourceNotFoundException("El residuo no existe");
                }
        );
    }
}
