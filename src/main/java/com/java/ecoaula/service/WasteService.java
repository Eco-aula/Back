package com.java.ecoaula.service;

import org.springframework.stereotype.Service;

import com.java.ecoaula.entity.Waste;

@Service
public interface WasteService {
    public Waste createWaste(Waste waste);
    public Waste updateWaste(Integer id,Waste upWaste);
    public void deleteWaste(Integer id);
    
}
