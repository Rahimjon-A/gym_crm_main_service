package example.com.springcoretask1.service.impl;

import example.com.springcoretask1.dao.TraineeDAO;
import example.com.springcoretask1.dto.TraineeDTO;
import example.com.springcoretask1.exception.EntityNotFoundException;
import example.com.springcoretask1.model.Trainee;
import example.com.springcoretask1.service.TraineeService;
import example.com.springcoretask1.util.CredentialsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TraineeServiceImpl implements TraineeService {
    private static final Logger log = LoggerFactory.getLogger(TraineeServiceImpl.class);
    private TraineeDAO traineeDao;
    private CredentialsUtil credentialsUtil;

    @Autowired
    public void setTraineeDao(TraineeDAO traineeDao) {
        this.traineeDao = traineeDao;
    }

    @Autowired
    public void setCredentialsUtil(CredentialsUtil credentialsUtil) {
        this.credentialsUtil = credentialsUtil;
    }

    @Override
    public Trainee create(TraineeDTO dto) {
        log.info("Creating trainee: {} {}", dto.getFirstName(), dto.getLastName());

        Trainee trainee = new Trainee();
        trainee.setFirstName(dto.getFirstName());
        trainee.setLastName(dto.getLastName());
        trainee.setDateOfBirth(dto.getDateOfBirth());
        trainee.setAddress(dto.getAddress());

        String username = credentialsUtil.generateUsername(dto.getFirstName(), dto.getLastName());
        String password = credentialsUtil.generatePassword();

        trainee.setUsername(username);
        trainee.setPassword(password);

        Trainee saved = traineeDao.save(trainee);
        log.info("Trainee created id={} username={}", saved.getUserId(), saved.getUsername());
        return saved;
    }

    @Override
    public Trainee update(Long userId, TraineeDTO dto) {
        log.info("Updating trainee id={}", userId);
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
            String newUsername = credentialsUtil.generateUsername(existing.getFirstName(), existing.getLastName());
            existing.setUsername(newUsername);
            log.info("Trainee {} username updated to {}", userId, newUsername);
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
        log.info("Deleting trainee id={}", userId);
        if (traineeDao.findById(userId).isEmpty()) {
            throw new EntityNotFoundException("Trainee not found: " + userId);
        }
        traineeDao.delete(userId);
    }
}