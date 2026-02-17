package epam.com.gym.crm.util;

import epam.com.gym.crm.model.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CsvParserUtil {
    public static List<Trainer> parseTrainers(List<String> lines) {
        List<Trainer> trainers = new ArrayList<>();

        for (String line : lines) {
            String[] data = line.split(",");

            Trainer trainer = new Trainer();
            trainer.setId(Long.parseLong(data[0]));
            trainer.setFirstName(data[1]);
            trainer.setLastName(data[2]);
            trainer.setUsername(data[3]);
            trainer.setPassword(data[4]);
            trainer.setSpecialization(TrainingType.valueOf(data[5]));
            trainer.setActive(true);

            trainers.add(trainer);
        }

        return trainers;
    }

    public static List<Trainee> parseTrainees(List<String> lines) {
        List<Trainee> trainees = new ArrayList<>();

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
            trainee.setActive(true);

            trainees.add(trainee);
        }

        return trainees;
    }

    public static List<Training> parseTrainings(List<String> lines) {
        List<Training> trainings = new ArrayList<>();

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

            trainings.add(training);
        }

        return trainings;
    }
}
