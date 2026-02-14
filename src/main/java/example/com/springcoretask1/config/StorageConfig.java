package example.com.springcoretask1.config;

import example.com.springcoretask1.model.Trainee;
import example.com.springcoretask1.model.Trainer;
import example.com.springcoretask1.model.Training;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class StorageConfig {

    @Bean("trainerStorage")
    public Map<Long, Trainer> trainerStorage() {
        return new ConcurrentHashMap<>();
    }

    @Bean("traineeStorage")
    public Map<Long, Trainee> traineeStorage() {
        return new ConcurrentHashMap<>();
    }

    @Bean("trainingStorage")
    public Map<Long, Training> trainingStorage() {
        return new ConcurrentHashMap<>();
    }
}