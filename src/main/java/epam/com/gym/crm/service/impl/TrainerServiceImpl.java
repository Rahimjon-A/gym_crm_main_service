package epam.com.gym.crm.service.impl;

import epam.com.gym.crm.dao.TrainerDAO;
import epam.com.gym.crm.dao.TrainingTypeDAO;
import epam.com.gym.crm.dto.trainer.TrainerDTO;
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
    public Trainer create(TrainerDTO dto) {
        validate(dto);

        log.info("Creating trainer profile for: {} {}", dto.getFirstName(), dto.getLastName());

        Trainer trainer = new Trainer();
        trainer.setFirstName(dto.getFirstName().trim());
        trainer.setLastName(dto.getLastName().trim());
        trainer.setActive(dto.getIsActive());

        trainer.setUsername(userService.generateUsername(dto.getFirstName(), dto.getLastName()));
        trainer.setPassword(userService.generatePassword());

        TrainingType tt = trainingTypeDAO.findById(dto.getSpecializationId())
                .orElseThrow(() -> new EntityNotFoundException("Invalid specialization id: " + dto.getSpecializationId()));
        trainer.setSpecialization(tt);

        Trainer saved = trainerDao.create(trainer);
        log.info("Trainer created with username: {}", saved.getUsername());
        return saved;
    }

    @Override
    @Transactional
    public Trainer update(String username, TrainerDTO dto) {
        validate(dto);
        log.info("Updating trainer username={}", username);

        Trainer existing = findByUsername(username);

        if (!dto.getFirstName().equals(existing.getFirstName())) {
            existing.setFirstName(dto.getFirstName().trim());
        }
        if (!dto.getLastName().equals(existing.getLastName())) {
            existing.setLastName(dto.getLastName().trim());
        }

        if (!Objects.equals(dto.getSpecializationId(), existing.getSpecialization().getId())) {
            TrainingType tt = trainingTypeDAO.findById(dto.getSpecializationId())
                    .orElseThrow(() -> new EntityNotFoundException("Invalid specialization id: " + dto.getSpecializationId()));
            existing.setSpecialization(tt);
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

    private void validate(TrainerDTO dto) {
        if (dto == null) {
            throw new ValidationException("Trainer data is required");
        }
        if (dto.getFirstName() != null && dto.getFirstName().isBlank()) {
            throw new ValidationException("First name cannot be blank");
        }
        if (dto.getLastName() != null && dto.getLastName().isBlank()) {
            throw new ValidationException("Last name cannot be blank");
        }
        if (dto.getIsActive() == null) {
            throw new ValidationException("Active/Deactive flag must not be null");
        }
        if(dto.getSpecializationId() == null) {
            throw new ValidationException("Specialization cannot be null");
        }
    }
}
