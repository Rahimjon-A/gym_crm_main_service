package epam.com.gym.crm.service.impl;

import epam.com.gym.crm.dao.TraineeDAO;
import epam.com.gym.crm.dto.TraineeDTO;
import epam.com.gym.crm.exception.EntityNotFoundException;
import epam.com.gym.crm.model.Trainee;
import epam.com.gym.crm.service.CredentialService;
import epam.com.gym.crm.service.TraineeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TraineeServiceImpl implements TraineeService {
    private static final Logger LOG = LoggerFactory.getLogger(TraineeServiceImpl.class);

    @Autowired
    private TraineeDAO traineeDao;
    private CredentialService credentialService;

    @Override
    public Trainee create(TraineeDTO dto) {
        LOG.info("Creating trainee: {} {}", dto.getFirstName(), dto.getLastName());

        Trainee trainee = new Trainee();
        trainee.setFirstName(dto.getFirstName());
        trainee.setLastName(dto.getLastName());
        trainee.setDateOfBirth(dto.getDateOfBirth());
        trainee.setAddress(dto.getAddress());
        trainee.setActive(true);

        String username = credentialService.generateUsername(dto.getFirstName(), dto.getLastName());
        String password = credentialService.generatePassword();

        trainee.setUsername(username);
        trainee.setPassword(password);

        Trainee saved = traineeDao.save(trainee);
        LOG.info("Trainee created id={} username={}", saved.getId(), saved.getUsername());
        return saved;
    }

    @Override
    public Trainee update(Long userId, TraineeDTO dto) {
        LOG.info("Updating trainee id={}", userId);
        Trainee existing = traineeDao.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Trainee not found: " + userId));

        boolean nameChanged = false;
        if (dto.getFirstName() != null && !dto.getFirstName().equals(existing.getFirstName())) {
            existing.setFirstName(dto.getFirstName());
            nameChanged = true;
        }
        if (dto.getLastName() != null && !dto.getLastName().equals(existing.getLastName())) {
            existing.setLastName(dto.getLastName());
            nameChanged = true;
        }
        if (dto.getDateOfBirth() != null) {
            existing.setDateOfBirth(dto.getDateOfBirth());
        }
        if (dto.getAddress() != null) {
            existing.setAddress(dto.getAddress());
        }

        if (nameChanged) {
            String newUsername = credentialService.generateUsername(existing.getFirstName(), existing.getLastName());
            existing.setUsername(newUsername);
            LOG.info("Trainee {} username updated to {}", userId, newUsername);
        }

        return traineeDao.save(existing);
    }

    @Override
    public Optional<Trainee> findById(Long userId) {
        return traineeDao.findById(userId);
    }

    @Override
    public List<Trainee> findAll() {
        return traineeDao.findAll();
    }

    @Override
    public void delete(Long userId) {
        LOG.info("Deleting trainee id={}", userId);
        if (traineeDao.findById(userId).isEmpty()) {
            throw new EntityNotFoundException("Trainee not found: " + userId);
        }
        traineeDao.delete(userId);
    }

    @Autowired
    public void setCredentialService(CredentialService credentialService) {
        this.credentialService = credentialService;
    }
}