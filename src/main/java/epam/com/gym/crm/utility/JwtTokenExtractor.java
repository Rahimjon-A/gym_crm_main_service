package epam.com.gym.crm.utility;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtTokenExtractor {

    private static final String PREFIX_BEARER = "Bearer ";
    public static final String AUTHORIZATION = "Authorization";

    public String extractBearerToken(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith(PREFIX_BEARER)) {
            log.debug("JWT token extracted from request header");
            return authHeader;
        }

        log.warn("No Bearer token found in request header");
        return null;
    }
}
