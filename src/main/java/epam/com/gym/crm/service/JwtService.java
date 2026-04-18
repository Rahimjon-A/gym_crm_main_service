package epam.com.gym.crm.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String generateToken(UserDetails userDetails);
    String generateServiceToken();
    String extractUsername(String token);
    boolean isTokenValid(String token, UserDetails userDetails);
    boolean isServiceToken(String token);
}
