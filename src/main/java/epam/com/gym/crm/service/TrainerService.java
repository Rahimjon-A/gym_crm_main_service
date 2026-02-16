package epam.com.gym.crm.service;

import epam.com.gym.crm.dto.TrainerDTO;
import epam.com.gym.crm.model.Trainer;

public interface TrainerService extends BaseService<Trainer, TrainerDTO> {
    Trainer update(Long userId, TrainerDTO dto);
}