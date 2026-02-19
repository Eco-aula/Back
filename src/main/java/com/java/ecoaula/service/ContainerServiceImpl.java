package com.java.ecoaula.service;


import java.util.List;

import org.springframework.stereotype.Service;

import com.java.ecoaula.dto.ContainerSummaryDTO;
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

    String containerInfo =
            "Contenedor ID " + container.getId()
            + " (" + container.getAllowedCategory() + ")";

    for (User user : userRepo.findAll()) {

        switch (container.getState()) {

            case LIMIT -> emailService.send(
                user.getEmail(),
                containerInfo + " al 70%",
                containerInfo + " ha alcanzado el 70% de su capacidad."
            );

            case FULL -> emailService.send(
                user.getEmail(),
                containerInfo + " lleno",
                containerInfo + " ha superado el 90% de su capacidad."
            );

            case RECYCLING -> emailService.send(
                user.getEmail(),
                containerInfo + " en reciclaje",
                containerInfo + " ha entrado en proceso de reciclaje."
            );

            case EMPTY -> emailService.send(
                user.getEmail(),
                containerInfo + " vacío",
                containerInfo + " ha sido vaciado y vuelve a estar disponible."
            );
        }
    }
}



    @Override
    public Container getById(int id) {
        return containerRepo.findById(id)
        .orElseThrow(() -> new ContainerNotFoundException(id));
}

    @Override
    public List<ContainerSummaryDTO> getContainersSummary() {
        return containerRepo.findAll()
            .stream()
            .map(container -> new ContainerSummaryDTO(
                    container.getAllowedCategory(),
                    container.getState()
            ))
            .toList();
    }


    @Override
public void startRecycling(int containerId) {
    Container container = containerRepo.findById(containerId)
            .orElseThrow(() -> new ContainerNotFoundException(containerId));

    if (container.getState() != State.FULL) {
        throw new IllegalStateException(
            "Solo se puede reciclar un contenedor FULL"
        );
    }

    container.setState(State.RECYCLING);
    containerRepo.save(container);

    notifyUsers(container);
}


@Override
public void markAsEmpty(int containerId) {
    Container container = containerRepo.findById(containerId)
            .orElseThrow(() -> new ContainerNotFoundException(containerId));

    if (container.getState() != State.RECYCLING) {
        throw new IllegalStateException(
            "Solo se puede marcar como EMPTY un contenedor en RECYCLING"
        );
    }

    container.setFillPercentage(0);
    container.setState(State.EMPTY);
    containerRepo.save(container);

    notifyUsers(container);
}


    
}
