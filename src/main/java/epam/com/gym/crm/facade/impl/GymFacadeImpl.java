package epam.com.gym.crm.facade.impl;

import epam.com.gym.crm.dao.filter.TraineeTrainingFilter;
import epam.com.gym.crm.dao.filter.TrainerTrainingFilter;
import epam.com.gym.crm.dto.request.PasswordChangeRequest;
import epam.com.gym.crm.dto.request.trainee.TraineeCreateRequest;
import epam.com.gym.crm.dto.request.trainee.TraineeUpdateRequest;
import epam.com.gym.crm.dto.request.trainer.TrainerAssignmentRequest;
import epam.com.gym.crm.dto.request.trainer.TrainerCreateRequest;
import epam.com.gym.crm.dto.request.trainer.TrainerUpdateRequest;
import epam.com.gym.crm.dto.request.training.TrainingCreateRequest;
import epam.com.gym.crm.facade.GymFacade;
import epam.com.gym.crm.mapper.TraineeMapper;
import epam.com.gym.crm.mapper.TrainerMapper;
import epam.com.gym.crm.mapper.TrainingMapper;
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
    private final TraineeMapper traineeMapper;
    private final TrainerMapper trainerMapper;
    private final TrainingMapper trainingMapper;

    public GymFacadeImpl(TrainerService trainerService,
                         TraineeService traineeService,
                         TrainingService trainingService,
                         TrainingTypeService trainingTypeService,
                         UserService userService,
                         AuthService authService,
                         TraineeMapper traineeMapper,
                         TrainerMapper trainerMapper,
                         TrainingMapper trainingMapper) {
        this.trainerService = trainerService;
        this.traineeService = traineeService;
        this.trainingService = trainingService;
        this.trainingTypeService = trainingTypeService;
        this.userService = userService;
        this.authService = authService;
        this.traineeMapper = traineeMapper;
        this.trainerMapper = trainerMapper;
        this.trainingMapper = trainingMapper;
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

        Trainer newTrainer = trainerMapper.toEntity(dto);
        return trainerService.create(newTrainer);
    }

    @Override
    public Trainer updateTrainer(String username, TrainerUpdateRequest dto) {
        log.info("Facade: Updating trainer username={}", username);

        Trainer trainerUpdates = trainerMapper.toEntity(dto);
        return trainerService.update(username, trainerUpdates);
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
        Trainee newTrainee = traineeMapper.toEntity(dto);

        return traineeService.create(newTrainee);
    }

    @Override
    public Trainee updateTrainee(String username, TraineeUpdateRequest dto) {
        log.info("Facade: Updating trainee username={}", username);

        Trainee traineeUpdates = traineeMapper.toEntity(dto);

        return traineeService.update(username, traineeUpdates);
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

        Training newTraining = trainingMapper.toEntity(dto);
        return trainingService.create(newTraining);
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
    public List<Training> updateTraineeTrainings(String username, List<TrainerAssignmentRequest> requests) {
        log.info("Facade: Updating trainee trainings for username={}", username);

        List<Training> domainAssignments = trainingMapper.toTrainingEntityList(requests);
        return trainingService.updateTraineeTrainings(username, domainAssignments);
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
