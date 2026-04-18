package epam.com.gym.crm.service.impl;

import epam.com.gym.crm.client.TrainerWorkloadClient;
import epam.com.gym.crm.dto.request.trainer.TrainerWorkloadRequest;
import epam.com.gym.crm.model.Trainer;
import epam.com.gym.crm.model.Training;
import epam.com.gym.crm.service.TrainerWorkloadClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TrainerWorkloadClientServiceImpl implements TrainerWorkloadClientService {
    private TrainerWorkloadClient trainerWorkloadClient;

    @Autowired
    public void setTrainerWorkloadClient(TrainerWorkloadClient trainerWorkloadClient) {
        this.trainerWorkloadClient = trainerWorkloadClient;
    }

    @Override
    public void notifyAdd(Trainer trainer, Training training) {
        try {
            log.info("Notifying workload service: ADD for trainer: {}", trainer.getUsername());
            trainerWorkloadClient.addTraining(buildWorkloadRequest(trainer, training));
        } catch (Exception e) {
            log.error("Failed to notify workload service for ADD — trainer: {} — {}",
                    trainer.getUsername(), e.getMessage(), e);
        }
    }

    @Override
    public void notifyDelete(Trainer trainer, Training training) {
        try {
            log.info("Notifying workload service: DELETE for trainer: {}", trainer.getUsername());
            trainerWorkloadClient.deleteTraining(buildWorkloadRequest(trainer, training));
        } catch (Exception e) {
            log.error("Failed to notify workload service for DELETE — trainer: {} — {}",
                    trainer.getUsername(), e.getMessage(), e);
        }
    }

    private TrainerWorkloadRequest buildWorkloadRequest(Trainer trainer, Training training) {
        return TrainerWorkloadRequest.builder()
                .username(trainer.getUsername())
                .firstName(trainer.getFirstName())
                .lastName(trainer.getLastName())
                .isActive(trainer.isActive())
                .trainingDate(training.getTrainingDate())
                .trainingDuration(training.getTrainingDuration())
                .build();
    }
}
