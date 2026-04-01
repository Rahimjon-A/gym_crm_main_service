package epam.com.gym.crm.service.impl;

import epam.com.gym.crm.dao.UserDAO;
import epam.com.gym.crm.exception.EntityNotFoundException;
import epam.com.gym.crm.exception.ValidationException;
import epam.com.gym.crm.model.Trainee;
import epam.com.gym.crm.service.TraineeService;
import epam.com.gym.crm.service.UserService;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class TraineeServiceImpl implements TraineeService {

    @Autowired
    private UserDAO<Trainee> traineeDao;
    @Autowired
    private EntityManager entityManager;

    private UserService userService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public Trainee create(Trainee trainee) {
        validate(trainee);
        log.info("Creating profile for: {} {}", trainee.getFirstName(), trainee.getLastName());

        trainee.setUsername(userService.generateUsername(trainee.getFirstName(), trainee.getLastName()));
        String rawPassword = userService.generatePassword();

        trainee.setPassword(passwordEncoder.encode(rawPassword));
        Trainee saved = traineeDao.create(trainee);

        entityManager.detach(saved);
        saved.setPassword(rawPassword);

        return  saved;
    }

    @Override
    @Transactional
    public Trainee update(String username, Trainee update) {
         validate(update);
        log.info("Updating profile for username: {}", username);

        Trainee existing = findByUsername(username);

        if (update.getFirstName() != null && !update.getFirstName().equals(existing.getFirstName())) {
            existing.setFirstName(update.getFirstName());
        }
        if (update.getLastName() != null && !update.getLastName().equals(existing.getLastName())) {
            existing.setLastName(update.getLastName());
        }
        if (update.getDateOfBirth() != null && !update.getDateOfBirth().equals(existing.getDateOfBirth())) {
            existing.setDateOfBirth(update.getDateOfBirth());
        }
        if (update.getAddress() != null && !update.getAddress().equals(existing.getAddress())) {
            existing.setAddress(update.getAddress());
        }
        if (update.isActive() != existing.isActive()) {
            existing.setActive(update.isActive());
        }

        return traineeDao.update(existing);
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
    public Trainee findById(Long userId) {
        return traineeDao.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Trainee not found id: " + userId));
    }

    @Override
    public List<Trainee> findAll() {
        return traineeDao.findAll();
    }

    private void validate(Trainee trainee) {
        if (trainee == null) {
            throw new ValidationException("Trainee data must not be null");
        }
        if (trainee.getFirstName() == null || trainee.getFirstName().isBlank()) {
            throw new ValidationException("First name is mandatory");
        }
        if (trainee.getLastName() == null || trainee.getLastName().isBlank()) {
            throw new ValidationException("Last name is mandatory");
        }

        if (trainee.getDateOfBirth() != null && trainee.getDateOfBirth().after(new java.util.Date())) {
            throw new ValidationException("Date of birth must be in the past");
        }
    }
}
