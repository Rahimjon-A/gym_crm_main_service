package epam.com.gym.crm.aspect;

import epam.com.gym.crm.model.common.Credentials;
import epam.com.gym.crm.service.AuthService;
import org.aspectj.lang.ProceedingJoinPoint;
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

    @InjectMocks
    private AuthAspect authAspect;

    @Test
    void enforceAuthentication_shouldProceed_whenCredentialsAreValid() throws Throwable {
        Credentials creds = new Credentials("john.doe", "password123");
        Object[] args = {creds, "someOtherArg"};

        when(joinPoint.getArgs()).thenReturn(args);
        when(joinPoint.proceed()).thenReturn(new Object());

        assertDoesNotThrow(() -> authAspect.enforceAuthentication(joinPoint));

        verify(authService, times(1)).authenticate("john.doe", "password123");
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
        Credentials creds = new Credentials("john.doe", "password123");
        Object[] args = {100L, creds};

        when(joinPoint.getArgs()).thenReturn(args);
        when(joinPoint.proceed()).thenReturn(new Object());

        assertDoesNotThrow(() -> authAspect.enforceAuthentication(joinPoint));

        verify(authService).authenticate("john.doe", "password123");
    }

    @Test
    void enforceAuthentication_shouldPropagateException_whenAuthServiceFails() throws Throwable {
        Credentials creds = new Credentials("john.doe", "wrongPassword");
        Object[] args = {creds};

        when(joinPoint.getArgs()).thenReturn(args);

        doThrow(new SecurityException("Invalid credentials"))
                .when(authService).authenticate("john.doe", "wrongPassword");

        assertThrows(SecurityException.class, () -> authAspect.enforceAuthentication(joinPoint));
        verify(joinPoint, never()).proceed();
    }
}
