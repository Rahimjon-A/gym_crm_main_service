package epam.com.gym.crm.service.impl;

import epam.com.gym.crm.dao.TrainerDAO;
import epam.com.gym.crm.dao.TrainingTypeDAO;
import epam.com.gym.crm.dto.TrainerDTO;
import epam.com.gym.crm.exception.EntityNotFoundException;
import epam.com.gym.crm.model.Trainer;
import epam.com.gym.crm.model.TrainingType;
import epam.com.gym.crm.model.User;
import epam.com.gym.crm.service.CredentialService;
import epam.com.gym.crm.service.TrainerService;
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
    private CredentialService credentialService;

    @Autowired
    public void setCredentialService(CredentialService credentialService) {
        this.credentialService = credentialService;
    }

    @Override
    @Transactional
    public Trainer create(TrainerDTO dto) {
        validate(dto);

        log.info("Creating trainer profile for: {} {}", dto.getFirstName(), dto.getLastName());

        User user = new User();
        user.setFirstName(dto.getFirstName().trim());
        user.setLastName(dto.getLastName().trim());
        user.setIsActive(true);

        user.setUsername(credentialService.generateUsername(dto.getFirstName(), dto.getLastName()));
        user.setPassword(credentialService.generatePassword());

        Trainer trainer = new Trainer();
        trainer.setUser(user);

        TrainingType tt = trainingTypeDAO.findById(dto.getSpecializationId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid specialization id: " + dto.getSpecializationId()));
        trainer.setSpecialization(tt);

        Trainer saved = trainerDao.create(trainer);
        log.info("Trainer created with username: {}", saved.getUser().getUsername());
        return saved;
    }

    @Override
    @Transactional
    public Trainer update(Long trainerId, TrainerDTO dto) {
        validate(dto);
        log.info("Updating trainer id={}", trainerId);

        Trainer existing = findById(trainerId);

        User user = existing.getUser();

        if (!dto.getFirstName().equals(user.getFirstName())) {
            user.setFirstName(dto.getFirstName().trim());
        }
        if (!dto.getLastName().equals(user.getLastName())) {
            user.setLastName(dto.getLastName().trim());
        }

        if (!Objects.equals(dto.getSpecializationId(), existing.getSpecialization().getId())) {
            TrainingType tt = trainingTypeDAO.findById(dto.getSpecializationId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid specialization id: " + dto.getSpecializationId()));
            existing.setSpecialization(tt);
        }

        return trainerDao.update(existing);
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
        if (dto == null) throw new IllegalArgumentException("Trainer data is required");
        if (dto.getFirstName() != null && dto.getFirstName().isBlank()) {
            throw new IllegalArgumentException("First name cannot be blank");
        }
        if (dto.getLastName() != null && dto.getLastName().isBlank()) {
            throw new IllegalArgumentException("Last name cannot be blank");
        }
        if(dto.getSpecializationId() == null) {
            throw new IllegalArgumentException("Specialization cannot be null");
        }
    }
}
