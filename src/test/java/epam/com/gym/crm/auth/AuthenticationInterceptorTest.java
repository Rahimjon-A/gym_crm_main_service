package epam.com.gym.crm.auth;

import epam.com.gym.crm.exception.AuthenticationException;
import epam.com.gym.crm.model.common.Credentials;
import epam.com.gym.crm.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationInterceptorTest {
    private static final String USERNAME = "john.doe";
    private static final String PASSWORD = "secretPassword";
    private static final String PLAIN_CREDENTIALS = USERNAME + ":" + PASSWORD;

    private static final String ENCODED_CREDENTIALS = Base64.getEncoder().encodeToString(PLAIN_CREDENTIALS.getBytes());
    private static final String VALID_AUTH_HEADER = AuthenticationInterceptor.PREFIX_BASIC + ENCODED_CREDENTIALS;

    @Mock
    private AuthService authService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Object handler;

    @InjectMocks
    private AuthenticationInterceptor interceptor;

    @BeforeEach
    void setUp() {
        interceptor.setAuthService(authService);
    }

    @Test
    void preHandle_shouldReturnTrue_whenCredentialsAreValid() {
        when(request.getHeader(AuthenticationInterceptor.HEADER_AUTHORIZATION)).thenReturn(VALID_AUTH_HEADER);

        boolean result = interceptor.preHandle(request, response, handler);

        assertTrue(result);
        verify(authService, times(1)).authenticate(any(Credentials.class));
    }

    @Test
    void preHandle_shouldThrowException_whenAuthorizationHeaderIsMissing() {
        when(request.getHeader(AuthenticationInterceptor.HEADER_AUTHORIZATION)).thenReturn(null);

        AuthenticationException exception = assertThrows(AuthenticationException.class,
                () -> interceptor.preHandle(request, response, handler));

        assertEquals(AuthenticationInterceptor.ERROR_MISSING_HEADER, exception.getMessage());
        verify(authService, never()).authenticate(any());
    }

    @Test
    void preHandle_shouldThrowException_whenAuthorizationHeaderDoesNotStartWithBasic() {
        when(request.getHeader(AuthenticationInterceptor.HEADER_AUTHORIZATION)).thenReturn("Bearer some-jwt-token");

        AuthenticationException exception = assertThrows(AuthenticationException.class,
                () -> interceptor.preHandle(request, response, handler));

        assertEquals(AuthenticationInterceptor.ERROR_MISSING_HEADER, exception.getMessage());
        verify(authService, never()).authenticate(any());
    }

    @Test
    void preHandle_shouldThrowException_whenBase64IsMalformed() {
        when(request.getHeader(AuthenticationInterceptor.HEADER_AUTHORIZATION)).thenReturn("Basic !@#$%^&*()");

        AuthenticationException exception = assertThrows(AuthenticationException.class,
                () -> interceptor.preHandle(request, response, handler));

        assertEquals(AuthenticationInterceptor.ERROR_INVALID_FORMAT, exception.getMessage());
        verify(authService, never()).authenticate(any());
    }

    @Test
    void preHandle_shouldPropagateException_whenAuthServiceFails() {
        when(request.getHeader(AuthenticationInterceptor.HEADER_AUTHORIZATION)).thenReturn(VALID_AUTH_HEADER);

        doThrow(new AuthenticationException("Invalid credentials"))
                .when(authService).authenticate(any(Credentials.class));

        assertThrows(AuthenticationException.class, () -> interceptor.preHandle(request, response, handler));
    }
}
