package example.com.springcoretask1.facade.impl;

import example.com.springcoretask1.dto.TraineeDTO;
import example.com.springcoretask1.dto.TrainerDTO;
import example.com.springcoretask1.dto.TrainingDTO;
import example.com.springcoretask1.facade.GymFacade;
import example.com.springcoretask1.model.Trainee;
import example.com.springcoretask1.model.Trainer;
import example.com.springcoretask1.model.Training;
import example.com.springcoretask1.service.TraineeService;
import example.com.springcoretask1.service.TrainerService;
import example.com.springcoretask1.service.TrainingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class GymFacadeImpl implements GymFacade {
    private static final Logger log = LoggerFactory.getLogger(GymFacadeImpl.class);
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
        log.info("Facade: createTrainer {} {}", dto.getFirstName(), dto.getLastName());
        return trainerService.create(dto);
    }

    @Override
    public Trainer updateTrainer(Long userId, TrainerDTO dto) {
        log.info("Facade: updateTrainer id={}", userId);
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
        log.info("Facade: createTrainee {} {}", dto.getFirstName(), dto.getLastName());
        return traineeService.create(dto);
    }

    @Override
    public Trainee updateTrainee(Long userId, TraineeDTO dto) {
        log.info("Facade: updateTrainee id={}", userId);
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
        log.info("Facade: deleteTrainee id={}", userId);
        traineeService.delete(userId);
    }




    @Override
    public Training createTraining(TrainingDTO dto) {
        log.info("Facade: createTraining '{}' trainee={} trainer={}", dto.getTrainingName(), dto.getTraineeId(), dto.getTrainerId());
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