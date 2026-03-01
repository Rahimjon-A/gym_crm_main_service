package epam.com.gym.crm.service.impl;

import epam.com.gym.crm.dao.TraineeDAO;
import epam.com.gym.crm.dao.TrainerDAO;
import epam.com.gym.crm.dao.TrainingDAO;
import epam.com.gym.crm.dto.TraineeDTO;
import epam.com.gym.crm.exception.EntityNotFoundException;
import epam.com.gym.crm.model.Trainee;
import epam.com.gym.crm.model.Trainer;
import epam.com.gym.crm.model.Training;
import epam.com.gym.crm.model.User;
import epam.com.gym.crm.service.CredentialService;
import epam.com.gym.crm.service.TraineeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class TraineeServiceImpl implements TraineeService {
    @Autowired
    private TraineeDAO traineeDao;
    @Autowired
    private TrainingDAO trainingDao;
    @Autowired
    private TrainerDAO trainerDao;
    private CredentialService credentialService;

    @Autowired
    public void setCredentialService(CredentialService credentialService) {
        this.credentialService = credentialService;
    }

    @Override
    @Transactional
    public Trainee create(TraineeDTO dto) {
        validate(dto);
        log.info("Creating profile for: {} {}", dto.getFirstName(), dto.getLastName());

        User user = new User();
        user.setFirstName(dto.getFirstName().trim());
        user.setLastName(dto.getLastName().trim());
        user.setIsActive(true);
        user.setUsername(credentialService.generateUsername(dto.getFirstName(), dto.getLastName()));
        user.setPassword(credentialService.generatePassword());

        Trainee trainee = new Trainee();
        trainee.setUser(user);
        trainee.setDateOfBirth(dto.getDateOfBirth());

        if (dto.getAddress() != null) {
            trainee.setAddress(dto.getAddress().trim());
        }

        return traineeDao.create(trainee);
    }

    @Override
    @Transactional
    public Trainee update(Long traineeId, TraineeDTO dto) {
        validate(dto);
        log.info("Updating profile for id: {}", traineeId);

        Trainee existing = findById(traineeId);
        User user = existing.getUser();

        user.setFirstName(dto.getFirstName().trim());
        user.setLastName(dto.getLastName().trim());
        existing.setDateOfBirth(dto.getDateOfBirth());

        if (dto.getAddress() != null) {
            existing.setAddress(dto.getAddress().trim());
        }


        return traineeDao.update(existing);
    }

    @Override
    @Transactional
    public Trainee updateTraineeTrainings(Long traineeId, Map<Long, Long> trainingAndTrainerIds) {
        log.info("Updating trainee (id={}) trainings assignments: {}", traineeId, trainingAndTrainerIds);

        if (traineeId == null) {
            throw new IllegalArgumentException("Trainee id is required");
        }
        if (trainingAndTrainerIds == null || trainingAndTrainerIds.isEmpty()) {
            throw new IllegalArgumentException("At least one training->trainer mapping is required");
        }

        Trainee trainee = findById(traineeId);

        for (Map.Entry<Long, Long> entry : trainingAndTrainerIds.entrySet()) {
            Long trainingId = entry.getKey();
            Long trainerId = entry.getValue();

            if (trainingId == null || trainerId == null) {
                throw new IllegalArgumentException("Training id and trainer id must not be null");
            }

            Training training = trainingDao.findById(trainingId)
                    .orElseThrow(() -> new EntityNotFoundException("Training not found id: " + trainingId));

            if (!training.getTrainee().getId().equals(traineeId)) {
                throw new IllegalArgumentException(
                        "Training id " + trainingId + " does not belong to trainee id " + traineeId);
            }

            Trainer trainer = trainerDao.findById(trainerId)
                    .orElseThrow(() -> new EntityNotFoundException("Trainer not found id: " + trainerId));

            if (!Boolean.TRUE.equals(trainer.getUser().getIsActive())) {
                throw new IllegalStateException("Trainer id " + trainerId + " is not active");
            }

            training.setTrainer(trainer);
            trainingDao.update(training);
            log.info("Assigned trainer id {} to training id {}", trainerId, trainingId);
        }

        return traineeDao.findById(traineeId)
                .orElseThrow(() -> new EntityNotFoundException("Trainee not found after update: " + traineeId));
    }

    @Override
    public Trainee findByUsername(String username) {
        return traineeDao.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Trainee not found: " + username));
    }

    @Override
    @Transactional
    public void deleteByUsername(String username) {
        log.info("Hard deleting trainee: {}", username);
        Trainee trainee = findByUsername(username);
        traineeDao.delete(trainee.getId());
    }

    @Override
    public List<Trainer> getUnassignedTrainers(String traineeUsername) {
        return traineeDao.getUnassignedTrainers(traineeUsername);
    }

    @Override
    public Trainee findById(Long userId) {
        return traineeDao.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Trainee not found id: " + userId));
    }

    @Override
    public List<Trainee> findAll() {
        return traineeDao.findAll();
    }

    private void validate(TraineeDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Trainee data must not be null");
        }
        if (dto.getFirstName() == null || dto.getFirstName().isBlank()) {
            throw new IllegalArgumentException("First name is mandatory");
        }
        if (dto.getLastName() == null || dto.getLastName().isBlank()) {
            throw new IllegalArgumentException("Last name is mandatory");
        }
        if (dto.getDateOfBirth() != null && dto.getDateOfBirth().after(new java.util.Date())) {
            throw new IllegalArgumentException("Date of birth must be in the past");
        }
    }
}
