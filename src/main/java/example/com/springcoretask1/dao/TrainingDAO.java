package example.com.springcoretask1.dao;

import example.com.springcoretask1.model.Training;

import java.util.List;
import java.util.Optional;

public interface TrainingDAO {
    Training save(Training training);
    Optional<Training> findById(Long id);
    List<Training> findAll();
    void delete(Long id);
}