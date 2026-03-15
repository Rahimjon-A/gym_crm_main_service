package epam.com.gym.crm.auth;

import epam.com.gym.crm.exception.AuthenticationException;
import epam.com.gym.crm.model.common.Credentials;
import epam.com.gym.crm.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
@Component
public class AuthenticationInterceptor implements HandlerInterceptor {
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String PREFIX_BASIC = "Basic ";
    public static final String ERROR_MISSING_HEADER = "Missing Authorization header";
    public static final String ERROR_INVALID_FORMAT = "Invalid Basic Auth format";
    private static final String REGEX_SEPARATOR = ":";

    private AuthService authService;

    @Autowired
    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String authHeader = request.getHeader(HEADER_AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith(PREFIX_BASIC)) {
            log.error("Authentication failed: {}", ERROR_MISSING_HEADER);
            throw new AuthenticationException(ERROR_MISSING_HEADER);
        }

        try {
            String base64Credentials = authHeader.substring(PREFIX_BASIC.length()).trim();

            byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
            String credentialsStr = new String(credDecoded, StandardCharsets.UTF_8);

            String[] values = credentialsStr.split(REGEX_SEPARATOR, 2);

            if (values.length != 2) {
                throw new AuthenticationException(ERROR_INVALID_FORMAT);
            }

            String username = values[0];
            String password = values[1];

            log.info("Interceptor: Authenticating user: {}", username);
            authService.authenticate(new Credentials(username, password));

            return true;

        } catch (IllegalArgumentException e) {
            log.error("Authentication failed: {}", ERROR_INVALID_FORMAT);
            throw new AuthenticationException(ERROR_INVALID_FORMAT);
        }
    }
}

