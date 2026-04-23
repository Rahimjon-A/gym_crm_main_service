package epam.com.gym.crm.init;

import epam.com.gym.crm.model.Training;
import epam.com.gym.crm.service.TrainerWorkloadService;
import epam.com.gym.crm.service.TrainingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class TrainerWorkloadStartupRunner implements ApplicationRunner {

    private TrainingService trainingService;
    private TrainerWorkloadService workloadClientService;

    @Autowired
    public void setTrainingService(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    @Autowired
    public void setWorkloadClientService(TrainerWorkloadService workloadClientService) {
        this.workloadClientService = workloadClientService;
    }

    @Override
    public void run(ApplicationArguments args) {
        log.info("Main Service started. Reading initial data.sql records to populate ActiveMQ...");
        
        List<Training> allTrainings = trainingService.findAll();
        
        if (allTrainings != null && !allTrainings.isEmpty()) {
            for (Training training : allTrainings) {
                workloadClientService.notifyAdd(training.getTrainer(), training);
            }
            log.info("Successfully pushed {} initial trainings to ActiveMQ.", allTrainings.size());
        }
    }
}
