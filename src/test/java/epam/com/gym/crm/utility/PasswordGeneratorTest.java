package epam.com.gym.crm.utility;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordGeneratorTest {

    @Test
    void generate_shouldReturnPasswordWithCorrectLength() {
        String password = PasswordGenerator.generate();

        assertNotNull(password);
        assertEquals(10, password.length());
    }

    @Test
    void generate_shouldProduceDifferentPasswords() {
        String password1 = PasswordGenerator.generate();
        String password2 = PasswordGenerator.generate();

        assertNotEquals(password1, password2);
    }
}
