package example.com.springcoretask1.service;

import example.com.springcoretask1.dto.TraineeDTO;
import example.com.springcoretask1.model.Trainee;

import java.util.List;
import java.util.Optional;

public interface TraineeService {
    Trainee create(TraineeDTO dto);
    Trainee update(Long userId, TraineeDTO dto);
    Optional<Trainee> findById(Long userId);
    List<Trainee> findAll();
    void delete(Long userId);
}