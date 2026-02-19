package com.java.ecoaula.exception;

public class ContainerNotFoundException extends RuntimeException {
   public ContainerNotFoundException(Integer id) {
        super("Container con id " + id + " no encontrado");
    }
}
