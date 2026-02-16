package epam.com.gym.crm.facade;

import epam.com.gym.crm.dto.TraineeDTO;
import epam.com.gym.crm.dto.TrainerDTO;
import epam.com.gym.crm.dto.TrainingDTO;
import epam.com.gym.crm.model.Trainee;
import epam.com.gym.crm.model.Trainer;
import epam.com.gym.crm.model.Training;

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