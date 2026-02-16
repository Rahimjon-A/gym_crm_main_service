package epam.com.gym.crm.service;

import epam.com.gym.crm.dto.TrainingDTO;
import epam.com.gym.crm.model.Training;

import java.util.List;
import java.util.Optional;

public interface BaseService<T, DTO> {
    T create(DTO dto);
    Optional<T> findById(Long id);
    List<T> findAll();
}
