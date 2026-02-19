package com.java.ecoaula.service;

import org.springframework.stereotype.Service;

import com.java.ecoaula.entity.Waste;

@Service
public interface WasteService {
    Waste createWaste(Waste waste);
    Waste updateWaste(Integer id, Waste upWaste);
    void deleteWaste(Integer id);
    
}
