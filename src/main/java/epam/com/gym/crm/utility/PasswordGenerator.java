package epam.com.gym.crm.utility;

import java.security.SecureRandom;

public final class PasswordGenerator {
    private static final int PASSWORD_LENGTH = 10;
    private static final String CHAR_POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    private PasswordGenerator() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static String generate() {
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int index = RANDOM.nextInt(CHAR_POOL.length());
            password.append(CHAR_POOL.charAt(index));
        }

        return password.toString();
    }
}
