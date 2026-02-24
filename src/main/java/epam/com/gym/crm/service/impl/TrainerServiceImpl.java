package epam.com.gym.crm.service.impl;

import epam.com.gym.crm.dao.UserDAO;
import epam.com.gym.crm.dto.TrainerDTO;
import epam.com.gym.crm.exception.EntityNotFoundException;
import epam.com.gym.crm.model.Trainer;
import epam.com.gym.crm.service.CredentialService;
import epam.com.gym.crm.service.TrainerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrainerServiceImpl implements TrainerService {
    private static final Logger LOG = LoggerFactory.getLogger(TrainerServiceImpl.class);

    @Autowired
    private UserDAO<Trainer> trainerDao;
    private CredentialService credentialService;

    @Override
    public Trainer create(TrainerDTO dto) {
        LOG.info("Creating trainer: {} {}", dto.getFirstName(), dto.getLastName());

        Trainer trainer = new Trainer();
        trainer.setFirstName(dto.getFirstName());
        trainer.setLastName(dto.getLastName());
        trainer.setSpecialization(dto.getSpecialization());
        trainer.setActive(true);

        String username = credentialService.generateUsername(dto.getFirstName(), dto.getLastName());
        String password = credentialService.generatePassword();

        trainer.setUsername(username);
        trainer.setPassword(password);

        Trainer saved = trainerDao.create(trainer);
        LOG.info("Trainer created id={} username={}", saved.getId(), saved.getUsername());
        return saved;
    }

    @Override
    public Trainer update(Long userId, TrainerDTO dto) {
        LOG.info("Updating trainer id={}", userId);
        Trainer existing = trainerDao.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Trainer not found: " + userId));

        boolean nameChanged = false;
        if (dto.getFirstName() != null && !dto.getFirstName().equals(existing.getFirstName())) {
            existing.setFirstName(dto.getFirstName());
            nameChanged = true;
        }
        if (dto.getLastName() != null && !dto.getLastName().equals(existing.getLastName())) {
            existing.setLastName(dto.getLastName());
            nameChanged = true;
        }
        if (dto.getSpecialization() != null) {
            existing.setSpecialization(dto.getSpecialization());
        }

        if (nameChanged) {
            String newUsername = credentialService.generateUsername(existing.getFirstName(), existing.getLastName());
            existing.setUsername(newUsername);
            LOG.info("Trainer {} username updated to {}", userId, newUsername);
        }

        return trainerDao.update(existing);
    }

    @Override
    public Optional<Trainer> findById(Long userId) {
        return trainerDao.findById(userId);
    }

    @Override
    public List<Trainer> findAll() {
        return trainerDao.findAll();
    }


    @Autowired
    public void setCredentialService(CredentialService credentialService) {
        this.credentialService = credentialService;
    }
}
