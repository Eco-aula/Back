package com.java.ecoaula.service;

import com.java.ecoaula.entity.Container;

public interface ContainerService {

   public void updateFillPercentage(Integer containerId, float percentage);

   public void setRecycling(Integer containerId);

   public Container getById(Integer id);
}
