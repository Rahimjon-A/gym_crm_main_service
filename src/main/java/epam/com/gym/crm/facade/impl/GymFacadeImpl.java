package epam.com.gym.crm.facade.impl;

import epam.com.gym.crm.aspect.RequireAuth;
import epam.com.gym.crm.dto.*;
import epam.com.gym.crm.facade.GymFacade;
import epam.com.gym.crm.model.Trainee;
import epam.com.gym.crm.model.Trainer;
import epam.com.gym.crm.model.Training;
import epam.com.gym.crm.service.AuthService;
import epam.com.gym.crm.service.TraineeService;
import epam.com.gym.crm.service.TrainerService;
import epam.com.gym.crm.service.TrainingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class GymFacadeImpl implements GymFacade {

    private static final Logger LOG = LoggerFactory.getLogger(GymFacadeImpl.class);

    private final TrainerService trainerService;
    private final TraineeService traineeService;
    private final TrainingService trainingService;
    private final AuthService authService;

    public GymFacadeImpl(TrainerService trainerService,
                         TraineeService traineeService,
                         TrainingService trainingService,
                         AuthService authService) {
        this.trainerService = trainerService;
        this.traineeService = traineeService;
        this.trainingService = trainingService;
        this.authService = authService;
    }

    /* ================= TRAINER ================= */

    @Override
    public Trainer createTrainer(TrainerDTO dto) {
        LOG.info("Facade: Creating trainer {} {}", dto.getFirstName(), dto.getLastName());
        return trainerService.create(dto);
    }

    @Override
    @RequireAuth
    public Trainer updateTrainer(String username, String password, Long trainerId, TrainerDTO dto) {
        LOG.info("Facade: Updating trainer id={}", trainerId);
        return trainerService.update(trainerId, dto);
    }

    @Override
    @RequireAuth
    public void activateTrainer(String username, String password) {
        LOG.info("Facade: Activating trainer username={}", username);
        authService.activateUser(username);
    }

    @Override
    @RequireAuth
    public void deactivateTrainer(String username, String password) {
        LOG.info("Facade: Deactivating trainer username={}", username);
        authService.deactivateUser(username);
    }

    @Override
    @RequireAuth
    public void changeTrainerPassword(String username, String password, String newPassword) {
        LOG.info("Facade: Changing password for trainer username={}", username);
        authService.changePassword(username, password, newPassword);
    }

    @Override
    @RequireAuth
    public Trainer getTrainerById(String username, String password, Long trainerId) {
        LOG.info("Facade: Fetching trainer by id={}", trainerId);
        return trainerService.findById(trainerId);
    }

    @Override
    @RequireAuth
    public Trainer getTrainerByUserName(String username, String password) {
        LOG.info("Facade: Fetching trainer by username={}", username);
        return trainerService.findByUsername(username);
    }

    @Override
    @RequireAuth
    public List<Trainer> getAllTrainers(String username, String password) {
        LOG.info("Facade: Fetching all trainers");
        return trainerService.findAll();
    }

    /* ================= TRAINEE ================= */

    @Override
    public Trainee createTrainee(TraineeDTO dto) {
        LOG.info("Facade: Creating trainee {} {}", dto.getFirstName(), dto.getLastName());
        return traineeService.create(dto);
    }

    @Override
    @RequireAuth
    public Trainee updateTrainee(String username, String password, Long traineeId, TraineeDTO dto) {
        LOG.info("Facade: Updating trainee id={}", traineeId);
        return traineeService.update(traineeId, dto);
    }

    @Override
    @RequireAuth
    public Trainee updateTraineeTrainings(String username, String password, Long traineeId, Map<Long, Long> trainingAndTrainerIds) {
        LOG.info("Facade: Updating trainee trainings for traineeId={}", traineeId);
        return traineeService.updateTraineeTrainings(traineeId, trainingAndTrainerIds);
    }

    @Override
    @RequireAuth
    public void activateTrainee(String username, String password) {
        LOG.info("Facade: Activating trainee username={}", username);
        authService.activateUser(username);
    }

    @Override
    @RequireAuth
    public void deactivateTrainee(String username, String password) {
        LOG.info("Facade: Deactivating trainee username={}", username);
        authService.deactivateUser(username);
    }

    @Override
    @RequireAuth
    public void changeTraineePassword(String username, String password, String newPassword) {
        LOG.info("Facade: Changing password for trainee username={}", username);
        authService.changePassword(username, password, newPassword);
    }

    @Override
    @RequireAuth
    public Trainee getTraineeById(String username, String password, Long traineeId) {
        LOG.info("Facade: Fetching trainee by id={}", traineeId);
        return traineeService.findById(traineeId);
    }

    @Override
    @RequireAuth
    public Trainee getTraineeByUsername(String username, String password) {
        LOG.info("Facade: Fetching trainee by username={}", username);
        return traineeService.findByUsername(username);
    }

    @Override
    @RequireAuth
    public List<Trainer> getUnassignedTrainersOfTrainee(String username, String password) {
        LOG.info("Facade: Fetching unassigned trainers for trainee username={}", username);
        return traineeService.getUnassignedTrainers(username);
    }

    @Override
    @RequireAuth
    public List<Trainee> getAllTrainees(String username, String password) {
        LOG.info("Facade: Fetching all trainees");
        return traineeService.findAll();
    }

    @Override
    @RequireAuth
    public void deleteTrainee(String username, String password) {
        LOG.info("Facade: Hard deleting trainee username={}", username);
        traineeService.deleteByUsername(username);
    }

    /* ================= TRAINING ================= */

    @Override
    @RequireAuth
    public Training createTraining(String username, String password, TrainingDTO dto) {
        LOG.info("Facade: Creating training '{}' trainee={} trainer={}",
                dto.getTrainingName(), dto.getTraineeId(), dto.getTrainerId());
        return trainingService.create(dto);
    }

    @Override
    @RequireAuth
    public List<Training> getTraineeTrainingsByCriteria(String username, String password, TraineeTrainingFilter filter) {
        LOG.info("Facade: Fetching trainee trainings by criteria for username={}",
                filter != null ? filter.getTraineeUsername() : null);
        return trainingService.getTraineeTrainingsByCriteria(filter);
    }

    @Override
    @RequireAuth
    public List<Training> getTrainerTrainingsByCriteria(String username, String password, TrainerTrainingFilter filter) {
        LOG.info("Facade: Fetching trainer trainings by criteria for username={}",
                filter != null ? filter.getTrainerUsername() : null);
        return trainingService.getTrainerTrainingsByCriteria(filter);
    }

    @Override
    @RequireAuth
    public Training getTrainingById(String username, String password, Long id) {
        LOG.info("Facade: Fetching training by id={}", id);
        return trainingService.findById(id);
    }

    @Override
    @RequireAuth
    public List<Training> getAllTrainings(String username, String password) {
        LOG.info("Facade: Fetching all trainings");
        return trainingService.findAll();
    }
}
