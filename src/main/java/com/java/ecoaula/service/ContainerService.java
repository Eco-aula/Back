package com.java.ecoaula.service;

import java.util.List;

import com.java.ecoaula.dto.ContainerSummaryDTO;
import com.java.ecoaula.entity.Container;

public interface ContainerService {

   public void updateFillPercentage(int containerId, float percentage);

   public void setRecycling(int containerId);

   public Container getById(int id);

   public List<ContainerSummaryDTO> getContainersSummary();

   public void startRecycling(int containerId);

   public void markAsEmpty(int containerId);
}
