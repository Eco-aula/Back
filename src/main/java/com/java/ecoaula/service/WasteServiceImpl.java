package com.java.ecoaula.service;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.java.ecoaula.entity.Container;
import com.java.ecoaula.entity.Waste;
import com.java.ecoaula.repository.ContainerRepository;
import com.java.ecoaula.repository.WasteRepository;

@Service
public class WasteServiceImpl implements WasteService{
    private final WasteRepository wasteRepository;
    private final ContainerRepository containerRepository;


    public WasteServiceImpl(WasteRepository wasteRepository,ContainerRepository containerRepository){
        this.wasteRepository=wasteRepository;
        this.containerRepository=containerRepository;
    }

  @Override
public Waste createWaste(Waste waste) {
    if (waste == null) {
        throw new IllegalArgumentException("El residuo no puede estar vacío");
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
        if(id==null) throw new IllegalArgumentException("El residuo no existe");
        if(upWaste==null) throw new IllegalArgumentException("El residuo no puede estar vacío");
        Waste existingWaste=wasteRepository.findById(id).orElseThrow(()->new RuntimeException("El residuo no existe en la base de datos"));
        existingWaste.setName(upWaste.getName());
        existingWaste.setDescription(upWaste.getDescription());
        existingWaste.setHeavy(upWaste.getHeavy());
        existingWaste.setCategory(upWaste.getCategory());
        Waste updatedWaste=wasteRepository.save(existingWaste);
        return updatedWaste;
    }

    @Override
    public void deleteWaste(Integer id) {
       if(id==null) throw new IllegalArgumentException("El residuo no existe");
       wasteRepository.findById(id).ifPresentOrElse(wasteRepository::delete, ()->{
        throw new RuntimeException("El residuo no existe");
       });
    }

    /*@Override
public List<String> getAllWaste() {
    return wasteRepository.findAll()
        .stream()
        .map(w -> "Residuo: " + w.getName() + " - Estado: " + w.getState())
        .toList();
}*/


    /*@Override
    public List<VolumeByCategoryDTO> getTotalVolumeByCategory() {
         return wasteRepository.getTotalVolumeByCategory();
        
    }*/


         /*@Override
        public Double getTotalVolumeByCategory(Category category) {
        return Optional.ofNullable(
        wasteRepository.getTotalVolumeByCategory(category)
        ).orElse(0.0);
    }*/

}
