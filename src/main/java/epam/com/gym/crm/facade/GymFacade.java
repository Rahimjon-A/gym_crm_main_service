package epam.com.gym.crm.facade;

import epam.com.gym.crm.dao.filter.TraineeTrainingFilter;
import epam.com.gym.crm.dao.filter.TrainerTrainingFilter;
import epam.com.gym.crm.dto.*;
import epam.com.gym.crm.model.Trainee;
import epam.com.gym.crm.model.Trainer;
import epam.com.gym.crm.model.Training;
import epam.com.gym.crm.model.common.Credentials;

import java.util.List;
import java.util.Map;

public interface GymFacade {

    /* ================= TRAINER ================= */
    Trainer createTrainer(TrainerDTO dto);

    Trainer updateTrainer(Credentials credentials, Long trainerId, TrainerDTO dto);

    List<Trainer> getUnassignedTrainersOfTrainee(Credentials credentials);

    void activateTrainer(Credentials credentials);

    void deactivateTrainer(Credentials credentials);

    void changeTrainerPassword(Credentials credentials, String newPassword);

    Trainer getTrainerById(Credentials credentials, Long trainerId);

    Trainer getTrainerByUserName(Credentials credentials);

    List<Trainer> getAllTrainers(Credentials credentials);

    /* ================= TRAINEE ================= */
    Trainee createTrainee(TraineeDTO dto);

    Trainee updateTrainee(Credentials credentials, Long traineeId, TraineeDTO dto);

    void activateTrainee(Credentials credentials);

    void deactivateTrainee(Credentials credentials);

    void changeTraineePassword(Credentials credentials, String newPassword);

    Trainee getTraineeById(Credentials credentials, Long traineeId);

    Trainee getTraineeByUsername(Credentials credentials);

    List<Trainee> getAllTrainees(Credentials credentials);

    void deleteTrainee(Credentials credentials);

    /* ================= TRAINING ================= */
    Training createTraining(Credentials credentials, TrainingDTO dto);

    List<Training> getTraineeTrainingsByCriteria(Credentials credentials, TraineeTrainingFilter filter);

    List<Training> getTrainerTrainingsByCriteria(Credentials credentials, TrainerTrainingFilter filter);

    List<Training> updateTraineeTrainings(Credentials credentials, Long traineeId, Map<Long, Long> trainingAndTrainerIds);

    Training getTrainingById(Credentials credentials, Long id);

    List<Training> getAllTrainings(Credentials credentials);
}
