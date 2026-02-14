package example.com.springcoretask1.service;

import example.com.springcoretask1.dto.TrainingDTO;
import example.com.springcoretask1.model.Training;

import java.util.List;
import java.util.Optional;

public interface TrainingService {
    Training create(TrainingDTO dto);
    Optional<Training> findById(Long id);
    List<Training> findAll();
}