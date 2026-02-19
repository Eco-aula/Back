package com.java.ecoaula.scheduler;

import com.java.ecoaula.entity.Container;
import com.java.ecoaula.entity.State;
import com.java.ecoaula.repository.ContainerRepository;
import com.java.ecoaula.service.EmailService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ContainerReminderScheduler {

    private final ContainerRepository containerRepository;
    private final EmailService emailService;

    public ContainerReminderScheduler(ContainerRepository containerRepository,
                                      EmailService emailService) {
        this.containerRepository = containerRepository;
        this.emailService = emailService;
    }
    @Scheduled(fixedRate = 6 * 60 * 60 * 1000)
    public void sendRemindersIfFull() {

        List<Container> fullContainers =
                containerRepository.findByState(State.FULL);

        for (Container container : fullContainers) {
            emailService.sendToAllUsers(
                "Recordatorio: contenedor lleno",
                "El contenedor de " + container.getAllowedCategory()
                + " sigue lleno. Por favor, vaciarlo."
            );
        }
    }
}
