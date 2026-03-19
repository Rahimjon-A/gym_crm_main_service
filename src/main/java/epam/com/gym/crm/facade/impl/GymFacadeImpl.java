package epam.com.gym.crm.facade.impl;

import epam.com.gym.crm.dao.filter.TraineeTrainingFilter;
import epam.com.gym.crm.dao.filter.TrainerTrainingFilter;
import epam.com.gym.crm.dto.request.PasswordChangeRequest;
import epam.com.gym.crm.dto.request.trainee.TraineeCreateRequest;
import epam.com.gym.crm.dto.request.trainer.TrainerAssignmentRequest;
import epam.com.gym.crm.dto.request.trainer.TrainerCreateRequest;
import epam.com.gym.crm.dto.request.training.TrainingCreateRequest;
import epam.com.gym.crm.facade.GymFacade;
import epam.com.gym.crm.model.Trainee;
import epam.com.gym.crm.model.Trainer;
import epam.com.gym.crm.model.Training;
import epam.com.gym.crm.model.TrainingType;
import epam.com.gym.crm.model.common.Credentials;
import epam.com.gym.crm.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class GymFacadeImpl implements GymFacade {

    private final TrainerService trainerService;
    private final TraineeService traineeService;
    private final TrainingService trainingService;
    private final TrainingTypeService trainingTypeService;
    private final UserService userService;
    private final AuthService authService;

    public GymFacadeImpl(TrainerService trainerService,
                         TraineeService traineeService,
                         TrainingService trainingService,
                         TrainingTypeService trainingTypeService,
                         UserService userService,
                         AuthService authService) {
        this.trainerService = trainerService;
        this.traineeService = traineeService;
        this.trainingService = trainingService;
        this.trainingTypeService = trainingTypeService;
        this.userService = userService;
        this.authService = authService;
    }

    /* ================= AUTH ================= */

    @Override
    public void login(Credentials credentials) {
        log.info("Facade: Login into profile {}", credentials.getUsername());

        authService.authenticate(credentials);
    }

    /* ================= USER ================= */
    @Override
    public void activateUser(String username) {
        log.info("Facade: Activating user username={}", username);
        userService.activateUser(username);
    }

    @Override
    public void deactivateUser(String username) {
        log.info("Facade: Deactivating user username={}", username);
        userService.deactivateUser(username);
    }

    @Override
    public void changePassword(PasswordChangeRequest passwordChangeRequest) {
        log.info("Facade: Updating password profile {}", passwordChangeRequest.username());

        userService.changePassword(
                passwordChangeRequest.username(),
                passwordChangeRequest.oldPassword(),
                passwordChangeRequest.newPassword());
    }

    /* ================= TRAINER ================= */

    @Override
    public Trainer createTrainer(TrainerCreateRequest dto) {
        log.info("Facade: Creating trainer {} {}", dto.getFirstName(), dto.getLastName());
        return trainerService.create(dto);
    }

    @Override
    public Trainer updateTrainer(String username, TrainerCreateRequest dto) {
        log.info("Facade: Updating trainer username={}", username);
        return trainerService.update(username, dto);
    }

    @Override
    public List<Trainer> getUnassignedTrainersOfTrainee(String username) {
        log.info("Facade: Fetching unassigned trainers for username={}", username);
        return trainerService.getUnassignedTrainers(username);
    }

    @Override
    public Trainer getTrainerById(Long trainerId) {
        log.info("Facade: Fetching trainer by id={}", trainerId);
        return trainerService.findById(trainerId);
    }

    @Override
    public Trainer getTrainerByUserName(String username) {
        log.info("Facade: Fetching trainer by username={}", username);
        return trainerService.findByUsername(username);
    }

    @Override
    public List<Trainer> getAllTrainers() {
        log.info("Facade: Fetching all trainers");
        return trainerService.findAll();
    }

    /* ================= TRAINEE ================= */

    @Override
    public Trainee createTrainee(TraineeCreateRequest dto) {
        log.info("Facade: Creating trainee {} {}", dto.getFirstName(), dto.getLastName());
        return traineeService.create(dto);
    }

    @Override
    public Trainee updateTrainee(String username,  TraineeCreateRequest dto) {
        log.info("Facade: Updating trainee username={}", username);
        return traineeService.update(username, dto);
    }

    @Override
    public Trainee getTraineeById(Long traineeId) {
        log.info("Facade: Fetching trainee by id={}", traineeId);
        return traineeService.findById(traineeId);
    }

    @Override
    public Trainee getTraineeByUsername(String username) {
        log.info("Facade: Fetching trainee by username={}", username);
        return traineeService.findByUsername(username);
    }

    @Override
    public List<Trainee> getAllTrainees() {
        log.info("Facade: Fetching all trainees");
        return traineeService.findAll();
    }

    @Override
    public void deleteTrainee(String username) {
        log.info("Facade: Hard deleting trainee username={}", username);
        traineeService.deleteByUsername(username);
    }

    /* ================= TRAINING ================= */

    @Override
    public Training createTraining(TrainingCreateRequest dto) {
        log.info("Facade: Creating training '{}' trainee={} trainer={}",
                dto.getTrainingName(), dto.getTraineeUsername(), dto.getTrainerUsername());
        return trainingService.create(dto);
    }

    @Override
    public List<Training> getTraineeTrainingsByCriteria(TraineeTrainingFilter filter) {
        log.info("Facade: Fetching trainee trainings by criteria");
        return trainingService.getTraineeTrainingsByCriteria(filter);
    }

    @Override
    public List<Training> getTrainerTrainingsByCriteria(TrainerTrainingFilter filter) {
        log.info("Facade: Fetching trainer trainings by criteria");
        return trainingService.getTrainerTrainingsByCriteria(filter);
    }

    @Override
    public List<Training> updateTraineeTrainings(String traineeUsername, List<TrainerAssignmentRequest> assignments) {
        log.info("Facade: Updating trainee trainings for trainee={}", traineeUsername);
        return trainingService.updateTraineeTrainings(traineeUsername, assignments);
    }

    @Override
    public Training getTrainingById(Long id) {
        log.info("Facade: Fetching training by id={}", id);
        return trainingService.findById(id);
    }

    @Override
    public List<Training> getAllTrainings() {
        log.info("Facade: Fetching all trainings");
        return trainingService.findAll();
    }

    /* ================= TRAINING TYPES ================= */
    @Override
    public List<TrainingType> getAllTrainingTypes() {
        return trainingTypeService.findAll();
    }

}
