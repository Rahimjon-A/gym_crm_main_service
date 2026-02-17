package epam.com.gym.crm.util;

import epam.com.gym.crm.model.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CsvParserUtil {
    private static final String CSV_DELIMITER = ",";

    public static List<Trainer> parseTrainers(List<String> lines) {
        List<Trainer> trainers = new ArrayList<>();

        for (String line : lines) {
            String[] data = line.split(CSV_DELIMITER);

            Trainer trainer = new Trainer();
            trainer.setFirstName(data[0]);
            trainer.setLastName(data[1]);
            trainer.setUsername(data[2]);
            trainer.setPassword(data[3]);
            trainer.setSpecialization(TrainingType.valueOf(data[4]));
            trainer.setActive(true);

            trainers.add(trainer);
        }

        return trainers;
    }

    public static List<Trainee> parseTrainees(List<String> lines) {
        List<Trainee> trainees = new ArrayList<>();

        for (String line : lines) {
            String[] data = line.split(CSV_DELIMITER);

            Trainee trainee = new Trainee();
            trainee.setFirstName(data[0]);
            trainee.setLastName(data[1]);
            trainee.setUsername(data[2]);
            trainee.setPassword(data[3]);
            trainee.setDateOfBirth(LocalDate.parse(data[4]));
            trainee.setAddress(data[5]);
            trainee.setActive(true);

            trainees.add(trainee);
        }

        return trainees;
    }

    public static List<Training> parseTrainings(List<String> lines) {
        List<Training> trainings = new ArrayList<>();

        for (String line : lines) {
            String[] data = line.split(CSV_DELIMITER);

            Training training = new Training();
            training.setTraineeId(Long.parseLong(data[0]));
            training.setTrainerId(Long.parseLong(data[1]));
            training.setTrainingName(data[2]);
            training.setTrainingType(TrainingType.valueOf(data[3]));
            training.setTrainingDate(LocalDate.parse(data[4]));
            training.setTrainingDuration(Double.parseDouble(data[5]));

            trainings.add(training);
        }

        return trainings;
    }
}
