package epam.com.gym.crm.facade;

import epam.com.gym.crm.dao.filter.TraineeTrainingFilter;
import epam.com.gym.crm.dao.filter.TrainerTrainingFilter;
import epam.com.gym.crm.dto.request.PasswordChangeRequest;
import epam.com.gym.crm.dto.request.trainee.TraineeCreateRequest;
import epam.com.gym.crm.dto.request.trainer.TrainerAssignmentRequest;
import epam.com.gym.crm.dto.request.trainer.TrainerCreateRequest;
import epam.com.gym.crm.dto.request.training.TrainingCreateRequest;
import epam.com.gym.crm.model.Trainee;
import epam.com.gym.crm.model.Trainer;
import epam.com.gym.crm.model.Training;
import epam.com.gym.crm.model.TrainingType;
import epam.com.gym.crm.model.common.Credentials;

import java.util.List;

public interface GymFacade {

    /* ================= AUTH ================= */
    void login(Credentials credentials);

    void activateUser(String username);

    void deactivateUser(String username);

    void changePassword(PasswordChangeRequest passwordChangeRequest);

    /* ================= TRAINER ================= */
    Trainer createTrainer(TrainerCreateRequest dto);

    Trainer updateTrainer(String username, TrainerCreateRequest dto);

    List<Trainer> getUnassignedTrainersOfTrainee(String username);

    Trainer getTrainerById(Long trainerId);

    Trainer getTrainerByUserName(String username);

    List<Trainer> getAllTrainers();

    /* ================= TRAINEE ================= */
    Trainee createTrainee(TraineeCreateRequest dto);

    Trainee updateTrainee(String username, TraineeCreateRequest dto);

    Trainee getTraineeById(Long traineeId);

    Trainee getTraineeByUsername(String username);

    List<Trainee> getAllTrainees();

    void deleteTrainee(String username);

    /* ================= TRAINING ================= */
    Training createTraining(TrainingCreateRequest dto);

    List<Training> getTraineeTrainingsByCriteria(TraineeTrainingFilter filter);

    List<Training> getTrainerTrainingsByCriteria(TrainerTrainingFilter filter);

    List<Training> updateTraineeTrainings(String traineeUsername, List<TrainerAssignmentRequest> assignments);

    Training getTrainingById(Long id);

    List<Training> getAllTrainings();

    /* ================= TRAINING TYPES ================= */
    List<TrainingType> getAllTrainingTypes();
}
