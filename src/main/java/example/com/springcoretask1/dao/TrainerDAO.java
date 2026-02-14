package example.com.springcoretask1.dao;

import example.com.springcoretask1.model.Trainer;

import java.util.List;
import java.util.Optional;

public interface TrainerDAO {
    Trainer save(Trainer trainer);
    Optional<Trainer> findById(Long id);
    List<Trainer> findAll();
    void delete(Long id);
}