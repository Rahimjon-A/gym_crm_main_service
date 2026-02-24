package epam.com.gym.crm.service.impl;

import epam.com.gym.crm.dao.BaseDAO;
import epam.com.gym.crm.dao.UserDAO;
import epam.com.gym.crm.dto.TrainingDTO;
import epam.com.gym.crm.exception.EntityNotFoundException;
import epam.com.gym.crm.model.Trainee;
import epam.com.gym.crm.model.Trainer;
import epam.com.gym.crm.model.Training;
import epam.com.gym.crm.service.TrainingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrainingServiceImpl implements TrainingService {
    private static final Logger LOG = LoggerFactory.getLogger(TrainingServiceImpl.class);

    @Autowired
    private BaseDAO<Training> trainingDao;
    @Autowired
    private UserDAO<Trainer> trainerDao;
    @Autowired
    private UserDAO<Trainee> traineeDao;

    @Override
    public Training create(TrainingDTO dto) {
        LOG.info("Creating training '{}' for trainee={} trainer={}", dto.getTrainingName(), dto.getTraineeId(), dto.getTrainerId());

        if (trainerDao.findById(dto.getTrainerId()).isEmpty()) {
            throw new EntityNotFoundException("Trainer not found: " + dto.getTrainerId());
        }

        if (traineeDao.findById(dto.getTraineeId()).isEmpty()) {
            throw new EntityNotFoundException("Trainee not found: " + dto.getTraineeId());
        }

        Training t = new Training();
        t.setTraineeId(dto.getTraineeId());
        t.setTrainerId(dto.getTrainerId());
        t.setTrainingName(dto.getTrainingName());
        t.setTrainingType(dto.getTrainingType());
        t.setTrainingDate(dto.getTrainingDate());
        t.setTrainingDuration(dto.getTrainingDuration());

        Training saved = trainingDao.create(t);
        LOG.info("Training created id={}", saved.getId());
        return saved;
    }

    @Override
    public Optional<Training> findById(Long id) {
        return trainingDao.findById(id);
    }

    @Override
    public List<Training> findAll() {
        return trainingDao.findAll();
    }
}
