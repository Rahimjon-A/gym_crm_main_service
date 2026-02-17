package epam.com.gym.crm.service;

import epam.com.gym.crm.repository.UsernameRepository;
import epam.com.gym.crm.security.PasswordGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CredentialService {
    private static final Logger LOG = LoggerFactory.getLogger(CredentialService.class);
    private static final String SEPARATOR = ".";
    private PasswordGenerator passwordGenerator;
    private UsernameRepository usernameRepository;

    public String generatePassword() {
        LOG.debug("Generating random password");
        String password = passwordGenerator.generate();
        LOG.debug("Password generated successfully");
        return password;
    }

    public String generateUsername(String firstName, String lastName) {
        if (firstName == null || lastName == null || firstName.isBlank() || lastName.isBlank()) {
            LOG.error("Attempt to generate username with invalid input: firstName='{}', lastName='{}'",
                    firstName, lastName);
            throw new IllegalArgumentException("First name and last name must not be null or blank");
        }

        String base = firstName.trim().toLowerCase() + SEPARATOR + lastName.trim().toLowerCase();

        LOG.debug("Generating username based on base='{}'", base);

        String candidate = base;
        int counter = 1;

        while (usernameRepository.exists(candidate)) {
            candidate = base + counter;
            counter++;
        }

        LOG.debug("Final username generated='{}'", candidate);
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
