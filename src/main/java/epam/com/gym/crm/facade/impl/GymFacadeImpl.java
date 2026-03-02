package epam.com.gym.crm.facade.impl;

import epam.com.gym.crm.aspect.RequireAuth;
import epam.com.gym.crm.filter.TraineeTrainingFilter;
import epam.com.gym.crm.filter.TrainerTrainingFilter;
import epam.com.gym.crm.dto.*;
import epam.com.gym.crm.facade.GymFacade;
import epam.com.gym.crm.model.Trainee;
import epam.com.gym.crm.model.Trainer;
import epam.com.gym.crm.model.Training;
import epam.com.gym.crm.model.common.Credentials;
import epam.com.gym.crm.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class GymFacadeImpl implements GymFacade {

    private final TrainerService trainerService;
    private final TraineeService traineeService;
    private final TrainingService trainingService;
    private final UserService userService;

    public GymFacadeImpl(TrainerService trainerService,
                         TraineeService traineeService,
                         TrainingService trainingService,
                         UserService userService) {
        this.trainerService = trainerService;
        this.traineeService = traineeService;
        this.trainingService = trainingService;
        this.userService = userService;
    }

    /* ================= TRAINER ================= */

    @Override
    public Trainer createTrainer(TrainerDTO dto) {
        log.info("Facade: Creating trainer {} {}", dto.getFirstName(), dto.getLastName());
        return trainerService.create(dto);
    }

    @Override
    @RequireAuth
    public Trainer updateTrainer(Credentials credentials, Long trainerId, TrainerDTO dto) {
        log.info("Facade: Updating trainer id={}", trainerId);
        return trainerService.update(trainerId, dto);
    }

    @Override
    @RequireAuth
    public List<Trainer> getUnassignedTrainersOfTrainee(Credentials credentials) {
        log.info("Facade: Fetching unassigned trainers for username={}", credentials.username());
        return trainerService.getUnassignedTrainers(credentials.username());
    }

    @Override
    @RequireAuth
    public void activateTrainer(Credentials credentials) {
        log.info("Facade: Activating trainer username={}", credentials.username());
        userService.activateUser(credentials.username());
    }

    @Override
    @RequireAuth
    public void deactivateTrainer(Credentials credentials) {
        log.info("Facade: Deactivating trainer username={}", credentials.username());
        userService.deactivateUser(credentials.username());
    }

    @Override
    @RequireAuth
    public void changeTrainerPassword(Credentials credentials, String newPassword) {
        log.info("Facade: Changing password for trainer username={}", credentials.username());
        userService.changePassword(credentials.username(), credentials.password(), newPassword);
    }

    @Override
    @RequireAuth
    public Trainer getTrainerById(Credentials credentials, Long trainerId) {
        log.info("Facade: Fetching trainer by id={}", trainerId);
        return trainerService.findById(trainerId);
    }

    @Override
    @RequireAuth
    public Trainer getTrainerByUserName(Credentials credentials) {
        log.info("Facade: Fetching trainer by username={}", credentials.username());
        return trainerService.findByUsername(credentials.username());
    }

    @Override
    @RequireAuth
    public List<Trainer> getAllTrainers(Credentials credentials) {
        log.info("Facade: Fetching all trainers");
        return trainerService.findAll();
    }

    /* ================= TRAINEE ================= */

    @Override
    public Trainee createTrainee(TraineeDTO dto) {
        log.info("Facade: Creating trainee {} {}", dto.getFirstName(), dto.getLastName());
        return traineeService.create(dto);
    }

    @Override
    @RequireAuth
    public Trainee updateTrainee(Credentials credentials, Long traineeId, TraineeDTO dto) {
        log.info("Facade: Updating trainee id={}", traineeId);
        return traineeService.update(traineeId, dto);
    }

    @Override
    @RequireAuth
    public void activateTrainee(Credentials credentials) {
        log.info("Facade: Activating trainee username={}", credentials.username());
        userService.activateUser(credentials.username());
    }

    @Override
    @RequireAuth
    public void deactivateTrainee(Credentials credentials) {
        log.info("Facade: Deactivating trainee username={}", credentials.username());
        userService.deactivateUser(credentials.username());
    }

    @Override
    @RequireAuth
    public void changeTraineePassword(Credentials credentials, String newPassword) {
        log.info("Facade: Changing password for trainee username={}", credentials.username());
        userService.changePassword(credentials.username(), credentials.password(), newPassword);
    }

    @Override
    @RequireAuth
    public Trainee getTraineeById(Credentials credentials, Long traineeId) {
        log.info("Facade: Fetching trainee by id={}", traineeId);
        return traineeService.findById(traineeId);
    }

    @Override
    @RequireAuth
    public Trainee getTraineeByUsername(Credentials credentials) {
        log.info("Facade: Fetching trainee by username={}", credentials.username());
        return traineeService.findByUsername(credentials.username());
    }

    @Override
    @RequireAuth
    public List<Trainee> getAllTrainees(Credentials credentials) {
        log.info("Facade: Fetching all trainees");
        return traineeService.findAll();
    }

    @Override
    @RequireAuth
    public void deleteTrainee(Credentials credentials) {
        log.info("Facade: Hard deleting trainee username={}", credentials.username());
        traineeService.deleteByUsername(credentials.username());
    }

    /* ================= TRAINING ================= */

    @Override
    @RequireAuth
    public Training createTraining(Credentials credentials, TrainingDTO dto) {
        log.info("Facade: Creating training '{}' trainee={} trainer={}",
                dto.getTrainingName(), dto.getTraineeId(), dto.getTrainerId());
        return trainingService.create(dto);
    }

    @Override
    @RequireAuth
    public List<Training> getTraineeTrainingsByCriteria(Credentials credentials, TraineeTrainingFilter filter) {
        log.info("Facade: Fetching trainee trainings by criteria");
        return trainingService.getTraineeTrainingsByCriteria(filter);
    }

    @Override
    @RequireAuth
    public List<Training> getTrainerTrainingsByCriteria(Credentials credentials, TrainerTrainingFilter filter) {
        log.info("Facade: Fetching trainer trainings by criteria");
        return trainingService.getTrainerTrainingsByCriteria(filter);
    }

    @Override
    @RequireAuth
    public List<Training> updateTraineeTrainings(Credentials credentials, Long traineeId, Map<Long, Long> trainingAndTrainerIds) {
        log.info("Facade: Updating trainee trainings for traineeId={}", traineeId);
        return trainingService.updateTraineeTrainings(traineeId, trainingAndTrainerIds);
    }

    @Override
    @RequireAuth
    public Training getTrainingById(Credentials credentials, Long id) {
        log.info("Facade: Fetching training by id={}", id);
        return trainingService.findById(id);
    }

    @Override
    @RequireAuth
    public List<Training> getAllTrainings(Credentials credentials) {
        log.info("Facade: Fetching all trainings");
        return trainingService.findAll();
    }
}
