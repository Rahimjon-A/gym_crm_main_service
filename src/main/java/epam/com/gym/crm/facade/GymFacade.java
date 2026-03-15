package epam.com.gym.crm.facade;

import epam.com.gym.crm.dao.filter.TraineeTrainingFilter;
import epam.com.gym.crm.dao.filter.TrainerTrainingFilter;
import epam.com.gym.crm.dto.request.PasswordChangeRequest;
import epam.com.gym.crm.dto.trainee.TraineeDTO;
import epam.com.gym.crm.dto.trainer.TrainerAssignmentDTO;
import epam.com.gym.crm.dto.trainer.TrainerDTO;
import epam.com.gym.crm.dto.training.TrainingDTO;
import epam.com.gym.crm.model.Trainee;
import epam.com.gym.crm.model.Trainer;
import epam.com.gym.crm.model.Training;
import epam.com.gym.crm.model.TrainingType;
import epam.com.gym.crm.model.common.Credentials;

import java.util.List;
import java.util.Map;

public interface GymFacade {

    /* ================= AUTH ================= */
    void login(Credentials credentials);

    void changePassword(PasswordChangeRequest passwordChangeRequest);

    /* ================= TRAINER ================= */
    Trainer createTrainer(TrainerDTO dto);

    Trainer updateTrainer(String username, TrainerDTO dto);

    List<Trainer> getUnassignedTrainersOfTrainee(String username);

    void activateTrainer(String username);

    void deactivateTrainer(String username);

    void changeTrainerPassword(Credentials credentials, String newPassword);

    Trainer getTrainerById(Long trainerId);

    Trainer getTrainerByUserName(String username);

    List<Trainer> getAllTrainers();

    /* ================= TRAINEE ================= */
    Trainee createTrainee(TraineeDTO dto);

    Trainee updateTrainee(String username, TraineeDTO dto);

    void activateTrainee(String username);

    void deactivateTrainee(String username);

    void changeTraineePassword(Credentials credentials, String newPassword);

    Trainee getTraineeById(Long traineeId);

    Trainee getTraineeByUsername(String username);

    List<Trainee> getAllTrainees();

    void deleteTrainee(String username);

    /* ================= TRAINING ================= */
    Training createTraining(TrainingDTO dto);

    List<Training> getTraineeTrainingsByCriteria(TraineeTrainingFilter filter);

    List<Training> getTrainerTrainingsByCriteria(TrainerTrainingFilter filter);

    List<Training> updateTraineeTrainings(String traineeUsername, List<TrainerAssignmentDTO> assignments);

    Training getTrainingById(Long id);

    List<Training> getAllTrainings();

    /* ================= TRAINING TYPES ================= */
    List<TrainingType> getAllTrainingTypes();
}
