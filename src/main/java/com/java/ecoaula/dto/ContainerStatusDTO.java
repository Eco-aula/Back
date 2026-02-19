package com.java.ecoaula.dto;

import com.java.ecoaula.entity.State;


public record ContainerStatusDTO(
    Integer id,
    float fillPercentage,
    State state
){}
