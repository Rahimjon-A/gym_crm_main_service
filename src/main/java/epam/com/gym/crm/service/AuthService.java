package epam.com.gym.crm.service;

import epam.com.gym.crm.model.common.Credentials;

public interface AuthService {
    void authenticate(Credentials credentials);
}
