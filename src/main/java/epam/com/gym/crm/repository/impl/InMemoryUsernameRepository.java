package epam.com.gym.crm.repository.impl;

import epam.com.gym.crm.dao.TraineeDAO;
import epam.com.gym.crm.dao.TrainerDAO;
import epam.com.gym.crm.repository.UsernameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InMemoryUsernameRepository implements UsernameRepository {
    private TrainerDAO trainerDAO;
    private TraineeDAO traineeDAO;

    @Override
    public boolean exists(String username) {
        return trainerDAO.existsByUsername(username)
                || traineeDAO.existsByUsername(username);
    }

    @Autowired
    public void setTrainerDAO(TrainerDAO trainerDAO) {
        this.trainerDAO = trainerDAO;
    }

    @Autowired
    public void setTraineeDAO(TraineeDAO traineeDAO) {
        this.traineeDAO = traineeDAO;
    }
}
