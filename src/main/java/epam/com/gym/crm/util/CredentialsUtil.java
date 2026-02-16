package epam.com.gym.crm.util;

import epam.com.gym.crm.dao.TraineeDAO;
import epam.com.gym.crm.dao.TrainerDAO;
import epam.com.gym.crm.model.Trainee;
import epam.com.gym.crm.model.Trainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;

@Component
public class CredentialsUtil {
    private static final Logger LOG = LoggerFactory.getLogger(CredentialsUtil.class);
    private static final String CHAR_POOL =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final Integer PASSWORD_LENGTH = 10;
    private static final String SEPARATOR_FOR_USERNAME = ".";
    private static final SecureRandom RANDOM = new SecureRandom();
    private TrainerDAO trainerDAO;
    private TraineeDAO traineeDAO;

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
            LOG.error("Username generation failed due to null or blank input");
            throw new IllegalArgumentException("First name and last name must not be null or blank");
        }

        String baseUsername = firstName.trim().toLowerCase() + SEPARATOR_FOR_USERNAME + lastName.trim().toLowerCase();
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

    public String generatePassword() {

        StringBuilder password = new StringBuilder();

        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int index = RANDOM.nextInt(CHAR_POOL.length());
            password.append(CHAR_POOL.charAt(index));
        }

        return password.toString();
    }

    private Set<String> collectAllUsernames() {

        LOG.debug("Collecting existing usernames from trainers and trainees");
        Set<String> usernames = new HashSet<>();

        for (Trainer trainer : trainerDAO.findAll()) {
            usernames.add(trainer.getUsername());
        }

        for (Trainee trainee : traineeDAO.findAll()) {
            usernames.add(trainee.getUsername());
        }
        LOG.debug("Collected {} existing usernames", usernames.size());

        return usernames;
    }
}