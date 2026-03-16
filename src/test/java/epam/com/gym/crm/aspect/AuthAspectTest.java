package epam.com.gym.crm.aspect;

import epam.com.gym.crm.model.common.Credentials;
import epam.com.gym.crm.service.AuthService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthAspectTest {

    @Mock
    private AuthService authService;

    @Mock
    private ProceedingJoinPoint joinPoint;

    @Mock
    private Credentials credentials;

    @InjectMocks
    private AuthAspect authAspect;

    @BeforeEach
    void setUp() {
        credentials = new Credentials("john.doe", "password123");
    }

    @Test
    void enforceAuthentication_shouldProceed_whenCredentialsAreValid() throws Throwable {
        Object[] args = {credentials, "someOtherArg"};

        when(joinPoint.getArgs()).thenReturn(args);
        when(joinPoint.proceed()).thenReturn(new Object());

        assertDoesNotThrow(() -> authAspect.enforceAuthentication(joinPoint));

        verify(authService, times(1)).authenticate(credentials);
        verify(joinPoint, times(1)).proceed();
    }

    @Test
    void enforceAuthentication_shouldThrowException_whenCredentialsMissing() {
        Object[] args = {"justAString", 123L};
        when(joinPoint.getArgs()).thenReturn(args);

        assertThrows(SecurityException.class, () -> authAspect.enforceAuthentication(joinPoint));

        verifyNoInteractions(authService);
    }

    @Test
    void enforceAuthentication_shouldFindCredentials_evenIfNotFirstArg() throws Throwable {
        Object[] args = {100L, credentials};

        when(joinPoint.getArgs()).thenReturn(args);
        when(joinPoint.proceed()).thenReturn(new Object());

        assertDoesNotThrow(() -> authAspect.enforceAuthentication(joinPoint));

        verify(authService).authenticate(credentials);
    }

    @Test
    void enforceAuthentication_shouldPropagateException_whenAuthServiceFails() throws Throwable {
        Object[] args = {credentials};

        when(joinPoint.getArgs()).thenReturn(args);

        doThrow(new SecurityException("Invalid credentials"))
                .when(authService).authenticate(credentials);

        assertThrows(SecurityException.class, () -> authAspect.enforceAuthentication(joinPoint));
        verify(joinPoint, never()).proceed();
    }
}
