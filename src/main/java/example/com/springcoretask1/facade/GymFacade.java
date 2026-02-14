package example.com.springcoretask1.facade;

import example.com.springcoretask1.dto.TraineeDTO;
import example.com.springcoretask1.dto.TrainerDTO;
import example.com.springcoretask1.dto.TrainingDTO;
import example.com.springcoretask1.model.Trainee;
import example.com.springcoretask1.model.Trainer;
import example.com.springcoretask1.model.Training;

import java.util.List;
import java.util.Optional;

public interface GymFacade {
    Trainer createTrainer(TrainerDTO dto);
    Trainer updateTrainer(Long userId, TrainerDTO dto);
    Optional<Trainer> getTrainerById(Long userId);
    List<Trainer> getAllTrainers();

    Trainee createTrainee(TraineeDTO dto);
    Trainee updateTrainee(Long userId, TraineeDTO dto);
    Optional<Trainee> getTraineeById(Long userId);
    List<Trainee> getAllTrainees();
    void deleteTrainee(Long userId);

    Training createTraining(TrainingDTO dto);
    Optional<Training> getTrainingById(Long id);
    List<Training> getAllTrainings();
}