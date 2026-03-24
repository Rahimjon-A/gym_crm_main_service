package epam.com.gym.crm.handler;

import epam.com.gym.crm.dto.response.ApiErrorResponse;
import epam.com.gym.crm.exception.AuthenticationException;
import epam.com.gym.crm.exception.EntityNotFoundException;
import epam.com.gym.crm.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {
    private static final String INTERNAL_NOT_FOUND_MSG = "User not found in DB table";
    private static final String INTERNAL_AUTH_MSG = "Invalid credentials hash";
    private static final String INTERNAL_VALIDATION_MSG = "Missing fields in DTO";
    private static final String INTERNAL_EXCEPTION_MSG = "Null pointer exception in service";

    private static final String EXPECTED_MSG_NOT_FOUND = "The requested resource could not be found.";
    private static final String EXPECTED_MSG_AUTH_FAILED = "Invalid username or password.";
    private static final String EXPECTED_MSG_VALIDATION_FAILED = "Validation failed. Please check your input.";
    private static final String EXPECTED_MSG_INTERNAL_ERROR = "An unexpected internal server error occurred. Please try again later.";

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void handleNotFound_shouldReturn404AndSafeMessage() {
        EntityNotFoundException ex = new EntityNotFoundException(INTERNAL_NOT_FOUND_MSG);

        ResponseEntity<ApiErrorResponse> response = exceptionHandler.handleNotFound(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(EXPECTED_MSG_NOT_FOUND, response.getBody().getMessage());
    }

    @Test
    void handleAuthentication_shouldReturn401AndSafeMessage() {
        AuthenticationException ex = new AuthenticationException(INTERNAL_AUTH_MSG);

        ResponseEntity<ApiErrorResponse> response = exceptionHandler.handleAuthentication(ex);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(EXPECTED_MSG_AUTH_FAILED, response.getBody().getMessage());
    }

    @Test
    void handleValidation_shouldReturn400AndSafeMessage() {
        ValidationException ex = new ValidationException(INTERNAL_VALIDATION_MSG);

        ResponseEntity<ApiErrorResponse> response = exceptionHandler.handleValidation(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(EXPECTED_MSG_VALIDATION_FAILED, response.getBody().getMessage());
    }

    @Test
    void handleGlobalException_shouldReturn500AndSafeMessage() {
        Exception ex = new Exception(INTERNAL_EXCEPTION_MSG);

        ResponseEntity<ApiErrorResponse> response = exceptionHandler.handleGlobalException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(EXPECTED_MSG_INTERNAL_ERROR, response.getBody().getMessage());
    }
}
