package com.java.ecoaula.service;

import com.java.ecoaula.entity.Container;

public interface ContainerService {

   public void updateFillPercentage(int containerId, float percentage);

   public void setRecycling(int containerId);

   public Container getById(int id);
}
