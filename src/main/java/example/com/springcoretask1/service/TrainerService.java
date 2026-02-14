package example.com.springcoretask1.service;

import example.com.springcoretask1.dto.TrainerDTO;
import example.com.springcoretask1.model.Trainer;

import java.util.List;
import java.util.Optional;

public interface TrainerService {
    Trainer create(TrainerDTO dto);
    Trainer update(Long userId, TrainerDTO dto);
    Optional<Trainer> findById(Long userId);
    List<Trainer> findAll();
}