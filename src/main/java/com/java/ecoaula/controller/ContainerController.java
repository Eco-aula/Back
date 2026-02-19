package com.java.ecoaula.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.java.ecoaula.dto.ContainerStatusDTO;
import com.java.ecoaula.dto.ContainerSummaryDTO;
import com.java.ecoaula.dto.UpdateFillDTO;
import com.java.ecoaula.entity.Container;
import com.java.ecoaula.service.ContainerService;


@RestController
@RequestMapping("/api/v1/containers")
public class ContainerController {

    private final ContainerService containerService;

    public ContainerController(ContainerService containerService) {
        this.containerService = containerService;
    }

    @PutMapping("/{id}/fill")
    public void updateFill(@PathVariable Integer id, @RequestBody UpdateFillDTO dto) {
        containerService.updateFillPercentage(id, dto.percentage());
    }

    @GetMapping("/{id}/status")
    public ContainerStatusDTO getStatus(@PathVariable Integer id) {
        Container container = containerService.getById(id);
        return new ContainerStatusDTO(
            container.getId(),
            container.getFillPercentage(),
            container.getState()
        );
    }

    @GetMapping("/summary")
    public ResponseEntity<List<ContainerSummaryDTO>> getContainersSummary() {

        List<ContainerSummaryDTO> containers =
                containerService.getContainersSummary();

        if (containers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(containers);
    }
    
}
