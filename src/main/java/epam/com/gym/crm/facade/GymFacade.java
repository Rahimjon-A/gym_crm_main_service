package epam.com.gym.crm.facade;

import epam.com.gym.crm.dto.*;
import epam.com.gym.crm.model.Trainee;
import epam.com.gym.crm.model.Trainer;
import epam.com.gym.crm.model.Training;

import java.util.List;
import java.util.Map;

public interface GymFacade {

    /* ================= TRAINER ================= */
    Trainer createTrainer(TrainerDTO dto);
    Trainer updateTrainer(String username, String password, Long trainerId, TrainerDTO dto);
    void activateTrainer(String username, String password);
    void deactivateTrainer(String username, String password);
    void changeTrainerPassword(String username, String password, String newPassword);
    Trainer getTrainerById(String username, String password, Long trainerId);
    Trainer getTrainerByUserName(String username, String password);
    List<Trainer> getAllTrainers(String username, String password);

    /* ================= TRAINEE ================= */
    Trainee createTrainee(TraineeDTO dto);
    Trainee updateTrainee(String username, String password, Long traineeId, TraineeDTO dto);
    Trainee updateTraineeTrainings(String username, String password, Long traineeId, Map<Long, Long> trainingAndTrainerIds);
    void activateTrainee(String username, String password);
    void deactivateTrainee(String username, String password);
    void changeTraineePassword(String username, String password, String newPassword);
    Trainee getTraineeById(String username, String password, Long traineeId);
    Trainee getTraineeByUsername(String username, String password);
    List<Trainer> getUnassignedTrainersOfTrainee(String username, String password);
    List<Trainee> getAllTrainees(String username, String password);
    void deleteTrainee(String username, String password);

    /* ================= TRAINING ================= */
    Training createTraining(String username, String password, TrainingDTO dto);
    List<Training> getTraineeTrainingsByCriteria(String username, String password, TraineeTrainingFilter filter);
    List<Training> getTrainerTrainingsByCriteria(String username, String password, TrainerTrainingFilter filter);
    Training getTrainingById(String username, String password, Long id);
    List<Training> getAllTrainings(String username, String password);
}
