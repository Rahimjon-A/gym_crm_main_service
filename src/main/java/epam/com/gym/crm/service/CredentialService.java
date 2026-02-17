package epam.com.gym.crm.service;

import epam.com.gym.crm.repository.UsernameRepository;
import epam.com.gym.crm.security.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CredentialService {
    private static final String SEPARATOR = ".";
    private PasswordGenerator passwordGenerator;
    private UsernameRepository usernameRepository;

    public String generatePassword() {
        return passwordGenerator.generate();
    }

    public String generateUsername(String firstName, String lastName) {
        if (firstName == null || lastName == null || firstName.isBlank() || lastName.isBlank()) {
            throw new IllegalArgumentException("First name and last name must not be null or blank");
        }

        String base = firstName.trim().toLowerCase() + SEPARATOR + lastName.trim().toLowerCase();

        String candidate = base;
        int counter = 1;

        while (usernameRepository.exists(candidate)) {
            candidate = base + counter;
            counter++;
        }

        return candidate;
    }

    @Autowired
    public void setPasswordGenerator(PasswordGenerator passwordGenerator) {
        this.passwordGenerator = passwordGenerator;
    }

    @Autowired
    public void setUsernameRepository(UsernameRepository usernameRepository) {
        this.usernameRepository = usernameRepository;
    }
}
