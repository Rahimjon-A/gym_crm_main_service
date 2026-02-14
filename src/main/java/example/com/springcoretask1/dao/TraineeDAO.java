package example.com.springcoretask1.dao;

import example.com.springcoretask1.model.Trainee;

import java.util.List;
import java.util.Optional;

public interface TraineeDAO {
    Trainee save(Trainee trainee);
    Optional<Trainee> findById(Long id);
    List<Trainee> findAll();
    void delete(Long id);
}