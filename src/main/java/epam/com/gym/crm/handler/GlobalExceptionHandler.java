package epam.com.gym.crm.handler;

import epam.com.gym.crm.dto.response.ApiErrorResponse;
import epam.com.gym.crm.exception.AuthenticationException;
import epam.com.gym.crm.exception.EntityNotFoundException;
import epam.com.gym.crm.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final String MSG_NOT_FOUND = "The requested resource could not be found.";
    private static final String MSG_AUTH_FAILED = "Invalid username or password.";
    private static final String MSG_INTERNAL_ERROR = "An unexpected internal server error occurred. Please try again later.";
    private static final String MSG_VALIDATION_FAILED = "Validation failed. Please check your input.";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_ERRORS = "errors";

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(EntityNotFoundException ex) {
        log.warn("Resource not found: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.NOT_FOUND, MSG_NOT_FOUND);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiErrorResponse> handleAuthentication(AuthenticationException ex) {
        log.warn("Authentication failed: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, MSG_AUTH_FAILED);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(ValidationException ex) {
        log.warn("Business validation failed: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, MSG_VALIDATION_FAILED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGlobalException(Exception ex) {
        log.error("Unhandled Internal Server Error: ", ex);
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, MSG_INTERNAL_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        log.warn("REST Payload validation failed.");

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put(KEY_MESSAGE, MSG_VALIDATION_FAILED);

        Map<String, String> fieldErrors = new HashMap<>();
        for (org.springframework.validation.FieldError error : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        }

        responseBody.put(KEY_ERRORS, fieldErrors);

        return new ResponseEntity<>(responseBody, headers, status);
    }

    private ResponseEntity<ApiErrorResponse> buildErrorResponse(HttpStatus status, String safeMessage) {
        ApiErrorResponse response = new ApiErrorResponse(safeMessage);
        return ResponseEntity.status(status).body(response);
    }
}
