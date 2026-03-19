package epam.com.gym.crm.service.impl;

import epam.com.gym.crm.dao.UserDAO;
import epam.com.gym.crm.dto.request.trainee.TraineeCreateRequest;
import epam.com.gym.crm.exception.EntityNotFoundException;
import epam.com.gym.crm.exception.ValidationException;
import epam.com.gym.crm.model.Trainee;
import epam.com.gym.crm.service.TraineeService;
import epam.com.gym.crm.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class TraineeServiceImpl implements TraineeService {
    @Autowired
    private UserDAO<Trainee> traineeDao;
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    @Transactional
    public Trainee create(TraineeCreateRequest dto) {
        validate(dto);
        log.info("Creating profile for: {} {}", dto.getFirstName(), dto.getLastName());

        Trainee trainee = new Trainee();
        trainee.setFirstName(dto.getFirstName().trim());
        trainee.setLastName(dto.getLastName().trim());
        trainee.setActive(dto.getIsActive());
        trainee.setUsername(userService.generateUsername(dto.getFirstName(), dto.getLastName()));
        trainee.setPassword(userService.generatePassword());
        trainee.setDateOfBirth(dto.getDateOfBirth());

        if (dto.getAddress() != null) {
            trainee.setAddress(dto.getAddress().trim());
        }

        return traineeDao.create(trainee);
    }

    @Override
    @Transactional
    public Trainee update(String username, TraineeCreateRequest dto) {
        validate(dto);
        log.info("Updating profile for username: {}", username);

        Trainee existing = findByUsername(username);

        if (!dto.getFirstName().equals(existing.getFirstName())) {
            existing.setFirstName(dto.getFirstName().trim());
        }
        if (!dto.getLastName().equals(existing.getLastName())) {
            existing.setLastName(dto.getLastName().trim());
        }
        if (dto.getDateOfBirth() != null && !dto.getDateOfBirth().equals(existing.getDateOfBirth())) {
            existing.setDateOfBirth(dto.getDateOfBirth());
        }
        if (dto.getAddress() != null && !dto.getAddress().equals(existing.getAddress())) {
            existing.setAddress(dto.getAddress().trim());
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

    private void validate(TraineeCreateRequest dto) {
        if (dto == null) {
            throw new ValidationException("Trainee data must not be null");
        }
        if (dto.getFirstName() == null || dto.getFirstName().isBlank()) {
            throw new ValidationException("First name is mandatory");
        }
        if (dto.getLastName() == null || dto.getLastName().isBlank()) {
            throw new ValidationException("Last name is mandatory");
        }
        if (dto.getIsActive() == null) {
            throw new ValidationException("Active/Deactive flag must not be null");
        }
        if (dto.getDateOfBirth() != null && dto.getDateOfBirth().after(new Date())) {
            throw new ValidationException("Date of birth must be in the past");
        };
    }
}
