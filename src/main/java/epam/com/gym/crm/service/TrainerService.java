package epam.com.gym.crm.service;

import epam.com.gym.crm.dto.TrainerDTO;
import epam.com.gym.crm.model.Trainer;

import java.util.List;

public interface TrainerService extends BaseService<Trainer, TrainerDTO> {
    Trainer update(Long userId, TrainerDTO dto);
    Trainer findByUsername(String username);
    List<Trainer> getUnassignedTrainers(String traineeUsername);
}
