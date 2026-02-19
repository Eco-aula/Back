package com.java.ecoaula.controller;

import java.util.List;
//import java.util.Locale.Category;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//import com.java.ecoaula.dto.VolumeByCategoryDTO;
import com.java.ecoaula.entity.Category;
import com.java.ecoaula.entity.Waste;
import com.java.ecoaula.service.WasteService;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;




@RestController
@RequestMapping("/api/v1/wastes")
@CrossOrigin("http://localhost:5173")
public class WasteController {
    private final WasteService wasteService;

    public WasteController(WasteService wasteService){
        this.wasteService=wasteService;
    }

    @PostMapping
    public ResponseEntity<Waste> createWaste(@RequestBody Waste waste) {
        Waste newWaste=wasteService.createWaste(waste);
        return new ResponseEntity<>(newWaste,HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Waste> updateWaste(@PathVariable Integer id, @RequestBody Waste waste) {
       Waste updWaste = wasteService.updateWaste(id, waste);
        return ResponseEntity.ok(updWaste);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteWaste(@PathVariable Integer id){
        wasteService.deleteWaste(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping
    public ResponseEntity<List<String>> getAllWaste() {
        List<String> wastes=wasteService.getAllWaste();
         if(wastes.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(wastes, HttpStatus.OK);
    }
    

    /*@GetMapping("/volume")
    public List<VolumeByCategoryDTO> getVolumeByCategory() {
        return wasteService.getTotalVolumeByCategory();
    }*/

    @GetMapping("/volume/{category}")
    public Double getVolumeByCategory(@PathVariable Category category) {
        return wasteService.getTotalVolumeByCategory(category);
}


    
}
