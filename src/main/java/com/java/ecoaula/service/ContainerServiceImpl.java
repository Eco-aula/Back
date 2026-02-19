package com.java.ecoaula.service;


import org.springframework.stereotype.Service;

import com.java.ecoaula.entity.Container;
import com.java.ecoaula.entity.State;
import com.java.ecoaula.entity.User;
import com.java.ecoaula.repository.ContainerRepository;
import com.java.ecoaula.repository.UserRepository;
import com.java.ecoaula.exception.ContainerNotFoundException;

@Service
public class ContainerServiceImpl implements ContainerService {

    private final ContainerRepository containerRepo;
    private final EmailService emailService;
    private final UserRepository userRepo;

    public ContainerServiceImpl(ContainerRepository containerRepo,
                                EmailService emailService,
                                UserRepository userRepo) {
        this.containerRepo = containerRepo;
        this.emailService = emailService;
        this.userRepo = userRepo;
    }

    @Override
    public void updateFillPercentage(int containerId, float percentage) {
        Container container = containerRepo.findById(containerId)
                .orElseThrow(() -> new RuntimeException("Container no encontrado"));

        container.setFillPercentage(percentage);

        State oldState = container.getState();
        State newState = calculateState(percentage);

        container.setState(newState);
        containerRepo.save(container);

        if (oldState != newState) {
            notifyUsers(container);
        }
    }

    @Override
    public void setRecycling(int containerId) {
        Container container = containerRepo.findById(containerId)
                .orElseThrow(() -> new RuntimeException("Container no encontrado"));

        if (container.getFillPercentage() < 70) {
            throw new IllegalStateException("No se puede reciclar si está por debajo del 70%");
        }

        container.setState(State.RECYCLING);
        containerRepo.save(container);
    }

    private State calculateState(float percentage) {
        if (percentage > 90) return State.FULL;
        if (percentage >= 70) return State.LIMIT;
        return State.EMPTY;
    }

    private void notifyUsers(Container container) {
        for (User user : userRepo.findAll()) {

            if (container.getState() == State.LIMIT) {
                emailService.send(
                    user.getEmail(),
                    "Contenedor al 70%" +
                    " ha alcanzado el 70% de su capacidad."
                );
            }

            if (container.getState() == State.FULL) {
                emailService.send(
                    user.getEmail(),
                    "URGENTE: Contenedor lleno" +
                    " ha superado el 90%."
                );
            }
        }
    }


    @Override
    public Container getById(int id) {
        return containerRepo.findById(id)
        .orElseThrow(() -> new ContainerNotFoundException(id));
}

}
