package epam.com.gym.crm.service.impl;

import epam.com.gym.crm.dao.TraineeDAO;
import epam.com.gym.crm.dao.TrainerDAO;
import epam.com.gym.crm.dao.TrainingDAO;
import epam.com.gym.crm.dao.TrainingTypeDAO;
import epam.com.gym.crm.dto.TraineeTrainingFilter;
import epam.com.gym.crm.dto.TrainerTrainingFilter;
import epam.com.gym.crm.dto.TrainingDTO;
import epam.com.gym.crm.exception.EntityNotFoundException;
import epam.com.gym.crm.model.Trainee;
import epam.com.gym.crm.model.Trainer;
import epam.com.gym.crm.model.Training;
import epam.com.gym.crm.model.TrainingType;
import epam.com.gym.crm.service.TrainingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class TrainingServiceImpl implements TrainingService {
    @Autowired
    private TrainingDAO trainingDao;

    @Autowired
    private TraineeDAO traineeDao;

    @Autowired
    private TrainerDAO trainerDao;

    @Autowired
    private TrainingTypeDAO trainingTypeDao;

    @Override
    @Transactional
    public Training create(TrainingDTO dto) {
        validate(dto);
        log.info("Creating training '{}' for trainee={} and trainer={}",
                dto.getTrainingName(), dto.getTraineeId(), dto.getTrainerId());

        Trainee trainee = traineeDao.findById(dto.getTraineeId())
                .orElseThrow(() -> new EntityNotFoundException("Trainee not found: " + dto.getTraineeId()));

        Trainer trainer = trainerDao.findById(dto.getTrainerId())
                .orElseThrow(() -> new EntityNotFoundException("Trainer not found: " + dto.getTrainerId()));

        TrainingType trainingType = trainingTypeDao.findById(dto.getTrainingTypeId())
                .orElseThrow(() -> new EntityNotFoundException("Training Type not found: " + dto.getTrainingTypeId()));

        Training training = new Training();
        training.setTrainee(trainee);
        training.setTrainer(trainer);
        training.setTrainingName(dto.getTrainingName().trim());
        training.setTrainingType(trainingType);
        training.setTrainingDate(dto.getTrainingDate());
        training.setTrainingDuration(dto.getTrainingDuration());

        return trainingDao.create(training);
    }

    @Override
    public List<Training> getTraineeTrainingsByCriteria(TraineeTrainingFilter filter) {
        if (filter == null || filter.getTraineeUsername() == null || filter.getTraineeUsername().isBlank()) {
            throw new IllegalArgumentException("Trainee username is required for filtering");
        }
        log.info("Fetching trainings for trainee: {}", filter.getTraineeUsername());
        return trainingDao.findTraineeTrainingsByCriteria(filter);
    }

    @Override
    public List<Training> getTrainerTrainingsByCriteria(TrainerTrainingFilter filter) {
        if (filter == null || filter.getTrainerUsername() == null || filter.getTrainerUsername().isBlank()) {
            throw new IllegalArgumentException("Trainer username is required for filtering");
        }
        log.info("Fetching trainings for trainer: {}", filter.getTrainerUsername());
        return trainingDao.findTrainerTrainingsByCriteria(filter);
    }

    @Override
    public Training findById(Long id) {
        return trainingDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Training not found id: " + id));
    }

    @Override
    public List<Training> findAll() {
        return trainingDao.findAll();
    }

    private void validate(TrainingDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Training data cannot be null");
        }
        if (dto.getTraineeId() == null) {
            throw new IllegalArgumentException("Trainee is mandatory");
        }
        if (dto.getTrainerId() == null) {
            throw new IllegalArgumentException("Trainer is mandatory");
        }
        if (dto.getTrainingName() == null || dto.getTrainingName().isBlank()) {
            throw new IllegalArgumentException("Training name is mandatory");
        }
        if (dto.getTrainingDate() == null) {
            throw new IllegalArgumentException("Training date is mandatory");
        }
        if (dto.getTrainingDuration() == null || dto.getTrainingDuration() <= 0) {
            throw new IllegalArgumentException("Training duration must be a positive number");
        }
        if (dto.getTrainingTypeId() == null) {
            throw new IllegalArgumentException("Training type  is mandatory");
        }
    }
}
