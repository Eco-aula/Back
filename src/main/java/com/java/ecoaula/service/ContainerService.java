package com.java.ecoaula.service;

import com.java.ecoaula.entity.Container;

public interface ContainerService {

   void updateFillPercentage(Integer containerId, float percentage);

   void setRecycling(Integer containerId);

   Container getById(Integer id);
}
