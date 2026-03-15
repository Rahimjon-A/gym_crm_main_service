package epam.com.gym.crm.handler;

import epam.com.gym.crm.exception.AuthenticationException;
import epam.com.gym.crm.exception.EntityNotFoundException;
import epam.com.gym.crm.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private static final String NOT_FOUND_MSG = "User not found";
    private static final String AUTH_MSG = "Invalid credentials";
    private static final String VALIDATION_MSG = "Missing fields";

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void handleNotFound_shouldReturn404() {
        EntityNotFoundException ex = new EntityNotFoundException(NOT_FOUND_MSG);

        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleNotFound(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(NOT_FOUND_MSG, response.getBody().get(GlobalExceptionHandler.KEY_MESSAGE));
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().get(GlobalExceptionHandler.KEY_STATUS));
        assertTrue(response.getBody().containsKey(GlobalExceptionHandler.KEY_TIMESTAMP));
        assertTrue(response.getBody().containsKey(GlobalExceptionHandler.KEY_TRANSACTION_ID));
    }

    @Test
    void handleAuthentication_shouldReturn401() {
        AuthenticationException ex = new AuthenticationException(AUTH_MSG);

        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleAuthentication(ex);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(AUTH_MSG, response.getBody().get(GlobalExceptionHandler.KEY_MESSAGE));
        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getBody().get(GlobalExceptionHandler.KEY_STATUS));
    }

    @Test
    void handleValidation_shouldReturn400() {
        ValidationException ex = new ValidationException(VALIDATION_MSG);

        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleValidation(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(VALIDATION_MSG, response.getBody().get(GlobalExceptionHandler.KEY_MESSAGE));
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().get(GlobalExceptionHandler.KEY_STATUS));
    }
}
