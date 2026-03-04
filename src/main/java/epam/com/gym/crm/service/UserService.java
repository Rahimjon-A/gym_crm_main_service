package epam.com.gym.crm.service;

public interface UserService {
    String generatePassword();
    String generateUsername(String firstName, String lastName);
    void changePassword(String username, String oldPassword, String newPassword);
    void activateUser(String username);
    void deactivateUser(String username);
}
