package epam.com.gym.crm.service.impl;

import epam.com.gym.crm.dao.TrainerDAO;
import epam.com.gym.crm.dao.TrainingDAO;
import epam.com.gym.crm.dao.UserDAO;
import epam.com.gym.crm.dao.filter.TraineeTrainingFilter;
import epam.com.gym.crm.dao.filter.TrainerTrainingFilter;
import epam.com.gym.crm.dto.request.trainer.TrainerAssignmentRequest;
import epam.com.gym.crm.exception.EntityNotFoundException;
import epam.com.gym.crm.exception.ValidationException;
import epam.com.gym.crm.model.Trainee;
import epam.com.gym.crm.model.Trainer;
import epam.com.gym.crm.model.Training;
import epam.com.gym.crm.service.TrainerWorkloadClientService;
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
    private UserDAO<Trainee> traineeDao;

    @Autowired
    private TrainerDAO trainerDao;

    private TrainerWorkloadClientService trainerWorkloadClientService;

    @Autowired
    public void setTrainerWorkloadClientService(TrainerWorkloadClientService trainerWorkloadClientService) {
        this.trainerWorkloadClientService = trainerWorkloadClientService;
    }

    @Override
    @Transactional
    public Training create(Training training) {
        validate(training);

        String traineeUsername = training.getTrainee().getUsername();
        String trainerUsername = training.getTrainer().getUsername();

        log.info("Creating training '{}' for trainee={} and trainer={}",
                training.getTrainingName(), traineeUsername, trainerUsername);

        Trainee realTrainee = traineeDao.findByUsername(traineeUsername)
                .orElseThrow(() -> new EntityNotFoundException("Trainee not found: " + traineeUsername));

        Trainer realTrainer = trainerDao.findByUsername(trainerUsername)
                .orElseThrow(() -> new EntityNotFoundException("Trainer not found: " + trainerUsername));

        training.setTrainee(realTrainee);
        training.setTrainer(realTrainer);
        training.setTrainingType(realTrainer.getSpecialization());

        Training saved = trainingDao.create(training);
        log.info("Training saved with id: {}", saved.getId());

        trainerWorkloadClientService.notifyAdd(realTrainer, saved);

        return saved;
    }

    @Override
    public List<Training> getTraineeTrainingsByCriteria(TraineeTrainingFilter filter) {
        if (filter == null || filter.getUsername() == null || filter.getUsername().isBlank()) {
            log.error("Attempted to fetch trainee trainings without a valid username.");
            throw new ValidationException("Trainee username is required for filtering.");
        }

        log.info("Fetching trainings for trainee: {}", filter.getUsername());
        return trainingDao.findTraineeTrainingsByCriteria(filter);
    }

    @Override
    public List<Training> getTrainerTrainingsByCriteria(TrainerTrainingFilter filter) {
        if (filter == null || filter.getUsername() == null || filter.getUsername().isBlank()) {
            log.error("Attempted to fetch trainer trainings without a valid username.");
            throw new ValidationException("Trainer username is required for filtering.");
        }

        log.info("Fetching trainings for trainer: {}", filter.getUsername());
        return trainingDao.findTrainerTrainingsByCriteria(filter);
    }

    @Override
    @Transactional
    public List<Training> updateTraineeTrainings(String traineeUsername, List<Training> assignments) {
        log.info("Updating trainings for trainee: {}", traineeUsername);

        Trainee trainee = traineeDao.findByUsername(traineeUsername)
                .orElseThrow(() -> new EntityNotFoundException("Trainee not found with username: " + traineeUsername));

        List<Training> updatedTrainings = new java.util.ArrayList<>();

        for (Training assignment : assignments) {
            Long trainingId = assignment.getId();
            String newTrainerUsername = assignment.getTrainer().getUsername();

            updatedTrainings.add(processSingleTrainingAssignment(
                    trainee.getUsername(),
                    trainingId,
                    newTrainerUsername
            ));
        }

        return updatedTrainings;
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

    private Training processSingleTrainingAssignment(String traineeUsername, Long trainingId, String trainerUsername) {

        Training training = trainingDao.findById(trainingId)
                .orElseThrow(() -> new EntityNotFoundException("Training not found id: " + trainingId));

        if (!training.getTrainee().getUsername().equals(traineeUsername)) {
            throw new ValidationException(String.format("Training id %d does not belong to trainee %s", trainingId, traineeUsername));
        }

        Trainer trainer = trainerDao.findByUsername(trainerUsername)
                .orElseThrow(() -> new EntityNotFoundException("Trainer not found username: " + trainerUsername));

        training.setTrainer(trainer);

        log.info("Assigned trainer {} to training id {}", trainerUsername, trainingId);
        return trainingDao.update(training);
    }

    private void validateUpdateInputs(String traineeUsername, List<TrainerAssignmentRequest> assignments) {
        if (traineeUsername == null || traineeUsername.isBlank()) {
            throw new ValidationException("Trainee id is required");
        }
        if (assignments == null || assignments.isEmpty()) {
            throw new ValidationException("At least one training->trainer mapping is required");
        }
    }

    private void validate(Training training) {
        if (training == null) {
            throw new ValidationException("Training data cannot be null");
        }
        if (training.getTrainee() == null || training.getTrainee().getUsername() == null) {
            throw new ValidationException("Trainee is mandatory");
        }
        if (training.getTrainer() == null || training.getTrainer().getUsername() == null) {
            throw new ValidationException("Trainer is mandatory");
        }
        if (training.getTrainingName() == null || training.getTrainingName().isBlank()) {
            throw new ValidationException("Training name is mandatory");
        }
        if (training.getTrainingDate() == null) {
            throw new ValidationException("Training date is mandatory");
        }
        if (training.getTrainingDuration() <= 0) {
            throw new ValidationException("Training duration must be a positive number");
        }
    }
}
