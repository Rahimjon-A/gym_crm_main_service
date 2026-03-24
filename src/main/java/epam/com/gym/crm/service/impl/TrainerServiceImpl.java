package epam.com.gym.crm.service.impl;

import epam.com.gym.crm.dao.TrainerDAO;
import epam.com.gym.crm.dao.TrainingTypeDAO;
import epam.com.gym.crm.dto.request.trainer.TrainerCreateRequest;
import epam.com.gym.crm.exception.EntityNotFoundException;
import epam.com.gym.crm.exception.ValidationException;
import epam.com.gym.crm.model.Trainer;
import epam.com.gym.crm.model.TrainingType;
import epam.com.gym.crm.service.TrainerService;
import epam.com.gym.crm.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class TrainerServiceImpl implements TrainerService {
    @Autowired
    private TrainerDAO trainerDao;
    @Autowired
    private TrainingTypeDAO trainingTypeDAO;
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    @Transactional
    public Trainer create(Trainer trainer) {
         validate(trainer);
        log.info("Creating trainer profile for: {} {}", trainer.getFirstName(), trainer.getLastName());

        trainer.setUsername(userService.generateUsername(trainer.getFirstName(), trainer.getLastName()));
        trainer.setPassword(userService.generatePassword());

        Long specId = trainer.getSpecialization().getId();
        TrainingType tt = trainingTypeDAO.findById(specId)
                .orElseThrow(() -> new EntityNotFoundException("Invalid specialization id: " + specId));
        trainer.setSpecialization(tt);

        Trainer saved = trainerDao.create(trainer);
        log.info("Trainer created with username: {}", saved.getUsername());
        return saved;
    }

    @Override
    @Transactional
    public Trainer update(String username, Trainer updates) {
         validate(updates);
        log.info("Updating trainer username={}", username);

        Trainer existing = findByUsername(username);

        if (updates.getFirstName() != null && !updates.getFirstName().equals(existing.getFirstName())) {
            existing.setFirstName(updates.getFirstName());
        }
        if (updates.getLastName() != null && !updates.getLastName().equals(existing.getLastName())) {
            existing.setLastName(updates.getLastName());
        }
        if (updates.isActive() != existing.isActive()) {
            existing.setActive(updates.isActive());
        }

        if (updates.getSpecialization() != null && updates.getSpecialization().getId() != null) {
            Long newSpecId = updates.getSpecialization().getId();
            if (!Objects.equals(newSpecId, existing.getSpecialization().getId())) {
                TrainingType tt = trainingTypeDAO.findById(newSpecId)
                        .orElseThrow(() -> new EntityNotFoundException("Invalid specialization id: " + newSpecId));
                existing.setSpecialization(tt);
            }
        }

        return trainerDao.update(existing);
    }

    @Override
    public List<Trainer> getUnassignedTrainers(String traineeUsername) {
        log.info("Fetching unassigned trainers for trainee username: {}", traineeUsername);

        if (traineeUsername == null || traineeUsername.isBlank()) {
            throw new ValidationException("Trainee username must not be null or blank");
        }

        return trainerDao.getUnassignedTrainers(traineeUsername);
    }

    @Override
    @Transactional(readOnly = true)
    public Trainer findById(Long id) {
        return trainerDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Trainer not found id: " + id));
    }

    @Override
    public Trainer findByUsername(String username) {
        return trainerDao.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Trainer not found: " + username));
    }

    @Override
    public List<Trainer> findAll() {
        return trainerDao.findAll();
    }

    private void validate(Trainer trainer) {
        if (trainer == null) {
            throw new ValidationException("Trainer data is required");
        }
        if (trainer.getFirstName() == null || trainer.getFirstName().isBlank()) {
            throw new ValidationException("First name cannot be blank");
        }
        if (trainer.getLastName() == null || trainer.getLastName().isBlank()) {
            throw new ValidationException("Last name cannot be blank");
        }
        if (trainer.getSpecialization() == null || trainer.getSpecialization().getId() == null) {
            throw new ValidationException("Specialization cannot be null");
        }
    }
}
