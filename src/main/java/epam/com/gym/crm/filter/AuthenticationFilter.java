package epam.com.gym.crm.filter;

import epam.com.gym.crm.exception.AuthenticationException;
import epam.com.gym.crm.model.common.Credentials;
import epam.com.gym.crm.service.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
@Component
public class AuthenticationFilter extends OncePerRequestFilter {
    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String PREFIX_BASIC = "Basic ";
    private static final String REGEX_SEPARATOR = ":";
    
    private static final String ERROR_MISSING_HEADER = "Missing Authorization header";
    private static final String ERROR_INVALID_FORMAT = "Invalid Basic Auth format";

    private static final String METHOD_POST = "POST";
    private static final String URL_TRAINEE_REGISTRATION = "/api/v1/trainees";
    private static final String URL_TRAINER_REGISTRATION = "/api/v1/trainers";
    private static final String URL_LOGIN = "/api/v1/auth/login";
    private static final String URL_SWAGGER_UI = "/swagger-ui";
    private static final String URL_API_DOCS = "/v3/api-docs";

    private AuthService authService;
    private HandlerExceptionResolver exceptionResolver;

    @Autowired
    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }

    @Autowired
    public void setExceptionResolver(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver exceptionResolver) {
        this.exceptionResolver = exceptionResolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                    HttpServletResponse response, 
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String authHeader = request.getHeader(HEADER_AUTHORIZATION);

            if (authHeader == null || !authHeader.startsWith(PREFIX_BASIC)) {
                log.error("Filter Authentication failed: {}", ERROR_MISSING_HEADER);
                throw new AuthenticationException(ERROR_MISSING_HEADER);
            }

            String base64Credentials = authHeader.substring(PREFIX_BASIC.length()).trim();
            byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
            String credentialsStr = new String(credDecoded, StandardCharsets.UTF_8);

            String[] values = credentialsStr.split(REGEX_SEPARATOR, 2);

            if (values.length != 2) {
                throw new AuthenticationException(ERROR_INVALID_FORMAT);
            }

            String username = values[0];
            String password = values[1];

            log.debug("Filter: Authenticating user: {}", username);
            authService.authenticate(new Credentials(username, password));

            filterChain.doFilter(request, response);

        } catch (IllegalArgumentException e) {
            log.error("Filter Authentication failed: {}", ERROR_INVALID_FORMAT);
            
            exceptionResolver.resolveException(request, response, null, new AuthenticationException(ERROR_INVALID_FORMAT));
        } catch (AuthenticationException e) {
            log.error("Filter Authentication failed: {}", ERROR_MISSING_HEADER);

            exceptionResolver.resolveException(request, response, null, new AuthenticationException(ERROR_MISSING_HEADER));
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();

        if (METHOD_POST.equalsIgnoreCase(method)) {
            if (path.startsWith(URL_TRAINEE_REGISTRATION) || path.startsWith(URL_TRAINER_REGISTRATION) || path.startsWith(URL_LOGIN)) {
                return true;
            }
        }

        return path.startsWith(URL_SWAGGER_UI) || path.startsWith(URL_API_DOCS);
    }
}