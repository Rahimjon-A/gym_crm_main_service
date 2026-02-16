package epam.com.gym.crm.service;

import epam.com.gym.crm.dto.TraineeDTO;
import epam.com.gym.crm.model.Trainee;

public interface TraineeService extends BaseService<Trainee, TraineeDTO> {
    Trainee update(Long userId, TraineeDTO dto);
    void delete(Long userId);
}