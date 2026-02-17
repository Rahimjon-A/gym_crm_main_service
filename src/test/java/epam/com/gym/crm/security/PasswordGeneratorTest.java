package epam.com.gym.crm.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordGeneratorTest {

    private final PasswordGenerator generator = new PasswordGenerator();

    @Test
    void generate_shouldReturnPasswordWithCorrectLength() {
        String password = generator.generate();

        assertNotNull(password);
        assertEquals(10, password.length());
    }

    @Test
    void generate_shouldProduceDifferentPasswords() {
        String password1 = generator.generate();
        String password2 = generator.generate();

        assertNotEquals(password1, password2);
    }
}
