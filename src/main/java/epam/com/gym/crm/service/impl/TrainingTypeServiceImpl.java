package epam.com.gym.crm.service.impl;

import epam.com.gym.crm.dao.TrainingTypeDAO;
import epam.com.gym.crm.model.TrainingType;
import epam.com.gym.crm.service.TrainingTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainingTypeServiceImpl implements TrainingTypeService {

    @Autowired
    private TrainingTypeDAO trainingTypeDAO;

    @Override
    public List<TrainingType> findAll() {
        return trainingTypeDAO.findAll();
    }

    @Override
    public Long count() {
        return  trainingTypeDAO.count();
    }
}
