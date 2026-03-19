package epam.com.gym.crm.service;

import epam.com.gym.crm.dto.request.trainer.TrainerCreateRequest;
import epam.com.gym.crm.model.Trainer;

import java.util.List;

public interface TrainerService extends AbstractUserService<Trainer, TrainerCreateRequest> {
    List<Trainer> getUnassignedTrainers(String traineeUsername);
}
