package com.java.ecoaula.service;

import java.util.List;

import com.java.ecoaula.dto.ContainerSummaryDTO;
import com.java.ecoaula.entity.Container;

public interface ContainerService {

   public void updateFillPercentage(Integer containerId, float percentage);

   public void setRecycling(Integer containerId);

   public Container getById(Integer id);

   public List<ContainerSummaryDTO> getContainersSummary();

   public void startRecycling(Integer containerId);

   public void markAsEmpty(Integer containerId);
}
