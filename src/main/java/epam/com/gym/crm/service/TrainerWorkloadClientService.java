package epam.com.gym.crm.service;

import epam.com.gym.crm.model.Trainer;
import epam.com.gym.crm.model.Training;

public interface TrainerWorkloadClientService {
    void notifyAdd(Trainer trainer, Training training);
    void notifyDelete(Trainer trainer, Training training);
}

