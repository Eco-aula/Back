package com.java.ecoaula.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.java.ecoaula.entity.Category;
//import com.java.ecoaula.dto.VolumeByCategoryDTO;
import com.java.ecoaula.entity.Waste;

@Service
public interface WasteService {
    public Waste createWaste(Waste waste);
    public Waste updateWaste(Integer id,Waste upWaste);
    public void deleteWaste(Integer id);
    public List<String> getAllWaste();
    //public List<VolumeByCategoryDTO> getTotalVolumeByCategory();
    public Double getTotalVolumeByCategory(Category category);
}
