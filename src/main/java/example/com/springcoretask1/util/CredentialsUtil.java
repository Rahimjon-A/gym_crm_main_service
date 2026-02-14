package example.com.springcoretask1.util;

import example.com.springcoretask1.dao.TraineeDAO;
import example.com.springcoretask1.dao.TrainerDAO;
import example.com.springcoretask1.model.Trainee;
import example.com.springcoretask1.model.Trainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;

@Component
public class CredentialsUtil {
    private static final Logger log = LoggerFactory.getLogger(CredentialsUtil.class);
    private TrainerDAO trainerDAO;
    private TraineeDAO traineeDAO;
    private static final String CHAR_POOL =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom random = new SecureRandom();

    @Autowired
    public void setTrainerDAO(TrainerDAO trainerDAO) {
        this.trainerDAO = trainerDAO;
    }

    @Autowired
    public void setTraineeDAO(TraineeDAO traineeDAO) {
        this.traineeDAO = traineeDAO;
    }

    public String generateUsername(String firstName, String lastName) {
        if (firstName == null || lastName == null || firstName.isBlank() || lastName.isBlank()) {
            log.error("Username generation failed due to null or blank input");
            throw new IllegalArgumentException("First name and last name must not be null or blank");
        }

        String baseUsername = firstName.trim().toLowerCase() + "." + lastName.trim().toLowerCase();
        Set<String> existingUsernames = collectAllUsernames();

        if (!existingUsernames.contains(baseUsername)) {
            return baseUsername;
        }

        int counter = 1;
        String newUsername;

        do {
            newUsername = baseUsername + counter;
            counter++;
        } while (existingUsernames.contains(newUsername));

        return newUsername;
    }

    private Set<String> collectAllUsernames() {

        log.debug("Collecting existing usernames from trainers and trainees");
        Set<String> usernames = new HashSet<>();

        for (Trainer trainer : trainerDAO.findAll()) {
            usernames.add(trainer.getUsername());
        }

        for (Trainee trainee : traineeDAO.findAll()) {
            usernames.add(trainee.getUsername());
        }
        log.debug("Collected {} existing usernames", usernames.size());

        return usernames;
    }

    public String generatePassword() {

        StringBuilder password = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            int index = random.nextInt(CHAR_POOL.length());
            password.append(CHAR_POOL.charAt(index));
        }

        return password.toString();
    }
}