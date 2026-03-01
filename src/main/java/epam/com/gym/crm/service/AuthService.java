package epam.com.gym.crm.service;

public interface AuthService {
    void authenticate(String username, String password);
    void changePassword(String username, String oldPassword, String newPassword);
    void activateUser(String username);
    void deactivateUser(String username);
}
