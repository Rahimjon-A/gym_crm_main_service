package example.com.springcoretask1.service.impl;

import example.com.springcoretask1.dao.TraineeDAO;
import example.com.springcoretask1.dao.TrainerDAO;
import example.com.springcoretask1.dao.TrainingDAO;
import example.com.springcoretask1.dto.TrainingDTO;
import example.com.springcoretask1.exception.EntityNotFoundException;
import example.com.springcoretask1.model.Training;
import example.com.springcoretask1.service.TrainingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrainingServiceImpl implements TrainingService {
    private static final Logger log = LoggerFactory.getLogger(TrainingServiceImpl.class);
    private TrainingDAO trainingDao;
    private TrainerDAO trainerDao;
    private TraineeDAO traineeDao;

    @Autowired
    public void setTrainingDao(TrainingDAO trainingDao) {
        this.trainingDao = trainingDao;
    }

    @Autowired
    public void setTrainerDao(TrainerDAO trainerDao) {
        this.trainerDao = trainerDao;
    }

    @Autowired
    public void setTraineeDao(TraineeDAO traineeDao) {
        this.traineeDao = traineeDao;
    }

    @Override
    public Training create(TrainingDTO dto) {
        log.info("Creating training '{}' for trainee={} trainer={}", dto.getTrainingName(), dto.getTraineeId(), dto.getTrainerId());

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

        Training saved = trainingDao.save(t);
        log.info("Training created id={}", saved.getTrainingId());
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