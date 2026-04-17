package epam.com.gym.crm.utility;

import epam.com.gym.crm.service.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class JwtTokenProvider {

    private static final String SERVICE_USERNAME = "gym-crm-main";
    private static final String PREFIX_BEARER    = "Bearer ";

    private JwtService jwtService;

    @Autowired
    public void setJwtService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public String generateServiceToken() {
        log.debug("Generating service token for inter-service call");

        UserDetails serviceUser = User
                .builder()
                .username(SERVICE_USERNAME)
                .password("")
                .authorities(List.of())
                .build();

        return PREFIX_BEARER + jwtService.generateToken(serviceUser);
    }
}