package epam.com.gym.crm.service;

import epam.com.gym.crm.dto.TrainerDTO;
import epam.com.gym.crm.model.Trainer;

import java.util.List;

public interface TrainerService extends AbstractUserService<Trainer, TrainerDTO> {
    List<Trainer> getUnassignedTrainers(String traineeUsername);
}
