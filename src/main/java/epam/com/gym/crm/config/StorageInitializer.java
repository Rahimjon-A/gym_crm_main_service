package epam.com.gym.crm.config;

import epam.com.gym.crm.model.Trainee;
import epam.com.gym.crm.model.Trainer;
import epam.com.gym.crm.model.Training;
import epam.com.gym.crm.model.TrainingType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Component
public class StorageInitializer implements BeanPostProcessor {

    @Value("${storage.trainer.path}")
    private String trainerPath;

    @Value("${storage.trainee.path}")
    private String traineePath;

    @Value("${storage.training.path}")
    private String trainingPath;

    private static final Logger log = LoggerFactory.getLogger(StorageInitializer.class);

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        if (beanName.equals("trainerStorage")) {
            log.info("Initializing trainer storage from trainers.csv");

            Map<Long, Trainer> map = (Map<Long, Trainer>) bean;
            loadTrainers(map);

            log.info("Loaded {} trainers", map.size());
        }

        if (beanName.equals("traineeStorage")) {
            log.info("Initializing trainer storage from trainees.csv");

            Map<Long, Trainee> map = (Map<Long, Trainee>) bean;
            loadTrainees(map);

            log.info("Loaded {} trainees", map.size());
        }

        if(beanName.equals("trainingStorage")) {
            log.info("Initializing trainer storage from trainings.csv");

            Map<Long, Training> map = (Map<Long, Training>) bean;
            loadTrainings(map);

            log.info("Loaded {} trainings", map.size());
        }

        return bean;
    }

    private void loadTrainers(Map<Long, Trainer> map) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(trainerPath));
            for (String line : lines) {
                String[] data = line.split(",");
                Trainer trainer = new Trainer();
                trainer.setId(Long.parseLong(data[0]));
                trainer.setFirstName(data[1]);
                trainer.setLastName(data[2]);
                trainer.setUsername(data[3]);
                trainer.setPassword(data[4]);
                trainer.setSpecialization(TrainingType.valueOf(data[5]));
                
                map.put(trainer.getId(), trainer);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load trainer data", e);
        }
    }

    private void loadTrainees(Map<Long, Trainee> map) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(traineePath));
            for (String line : lines) {
                String[] data = line.split(",");

                Trainee trainee = new Trainee();
                trainee.setId(Long.parseLong(data[0]));
                trainee.setFirstName(data[1]);
                trainee.setLastName(data[2]);
                trainee.setUsername(data[3]);
                trainee.setPassword(data[4]);
                trainee.setDateOfBirth(LocalDate.parse(data[5]));
                trainee.setAddress(data[6]);

                map.put(trainee.getId(), trainee);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load trainee data", e);
        }
    }

    private void loadTrainings(Map<Long, Training> map) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(trainingPath));
            for (String line : lines) {
                String[] data = line.split(",");
                Training training = new Training();
                training.setId(Long.parseLong(data[0]));
                training.setTraineeId(Long.parseLong(data[1]));
                training.setTrainerId(Long.parseLong(data[2]));
                training.setTrainingName(data[3]);
                training.setTrainingType(TrainingType.valueOf(data[4]));
                training.setTrainingDate(LocalDate.parse(data[5]));
                training.setTrainingDuration(Double.parseDouble(data[6]));

                map.put(training.getId(), training);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load trainer data", e);
        }
    }
}