package epam.com.gym.crm.facade.impl;

import epam.com.gym.crm.dto.TraineeDTO;
import epam.com.gym.crm.dto.TrainerDTO;
import epam.com.gym.crm.dto.TrainingDTO;
import epam.com.gym.crm.facade.GymFacade;
import epam.com.gym.crm.model.Trainee;
import epam.com.gym.crm.model.Trainer;
import epam.com.gym.crm.model.Training;
import epam.com.gym.crm.service.TraineeService;
import epam.com.gym.crm.service.TrainerService;
import epam.com.gym.crm.service.TrainingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class GymFacadeImpl implements GymFacade {
    private static final Logger LOG = LoggerFactory.getLogger(GymFacadeImpl.class);
    private final TrainerService trainerService;
    private final TraineeService traineeService;
    private final TrainingService trainingService;

    public GymFacadeImpl(TrainerService trainerService,
                         TraineeService traineeService,
                         TrainingService trainingService) {
        this.trainerService = trainerService;
        this.traineeService = traineeService;
        this.trainingService = trainingService;
    }

    @Override
    public Trainer createTrainer(TrainerDTO dto) {
        LOG.info("Facade: createTrainer {} {}", dto.getFirstName(), dto.getLastName());
        return trainerService.create(dto);
    }

    @Override
    public Trainer updateTrainer(Long userId, TrainerDTO dto) {
        LOG.info("Facade: updateTrainer id={}", userId);
        return trainerService.update(userId, dto);
    }

    @Override
    public Optional<Trainer> getTrainerById(Long userId) {
        return trainerService.findById(userId);
    }

    @Override
    public List<Trainer> getAllTrainers() {
        return trainerService.findAll();
    }




    @Override
    public Trainee createTrainee(TraineeDTO dto) {
        LOG.info("Facade: createTrainee {} {}", dto.getFirstName(), dto.getLastName());
        return traineeService.create(dto);
    }

    @Override
    public Trainee updateTrainee(Long userId, TraineeDTO dto) {
        LOG.info("Facade: updateTrainee id={}", userId);
        return traineeService.update(userId, dto);
    }

    @Override
    public Optional<Trainee> getTraineeById(Long userId) {
        return traineeService.findById(userId);
    }

    @Override
    public List<Trainee> getAllTrainees() {
        return traineeService.findAll();
    }

    @Override
    public void deleteTrainee(Long userId) {
        LOG.info("Facade: deleteTrainee id={}", userId);
        traineeService.delete(userId);
    }




    @Override
    public Training createTraining(TrainingDTO dto) {
        LOG.info("Facade: createTraining '{}' trainee={} trainer={}", dto.getTrainingName(), dto.getTraineeId(), dto.getTrainerId());
        return trainingService.create(dto);
    }

    @Override
    public Optional<Training> getTrainingById(Long id) {
        return trainingService.findById(id);
    }

    @Override
    public List<Training> getAllTrainings() {
        return trainingService.findAll();
    }
}