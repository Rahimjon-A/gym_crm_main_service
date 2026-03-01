package epam.com.gym.crm.aspect;

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

    @InjectMocks
    private AuthAspect authAspect;

    @BeforeEach
    void setUp() {
        authAspect.setAuthService(authService);
    }

    @Test
    void enforceAuthentication_shouldProceed_whenCredentialsAreValid() throws Throwable {
        Object[] validArgs = {"john.doe", "password123", "someOtherArg"};
        when(joinPoint.getArgs()).thenReturn(validArgs);
        when(joinPoint.proceed()).thenReturn(new Object());

        assertDoesNotThrow(() -> authAspect.enforceAuthentication(joinPoint));

        verify(authService, times(1)).authenticate("john.doe", "password123");
        verify(joinPoint, times(1)).proceed();
    }

    @Test
    void enforceAuthentication_shouldThrowException_whenArgsAreLessThanTwo() {
        Object[] invalidArgs = {"justUsername"};
        when(joinPoint.getArgs()).thenReturn(invalidArgs);

        assertThrows(SecurityException.class, () -> authAspect.enforceAuthentication(joinPoint));
        
        verifyNoInteractions(authService);
    }

    @Test
    void enforceAuthentication_shouldThrowException_whenArgsAreNotStrings() {
        Object[] invalidArgs = {123L, true};
        when(joinPoint.getArgs()).thenReturn(invalidArgs);

        assertThrows(SecurityException.class, () -> authAspect.enforceAuthentication(joinPoint));

        verifyNoInteractions(authService);
    }

    @Test
    void enforceAuthentication_shouldPropagateException_whenAuthServiceFails() throws Throwable {
        Object[] validArgs = {"john.doe", "wrongPassword"};
        when(joinPoint.getArgs()).thenReturn(validArgs);
        
        doThrow(new IllegalArgumentException("Invalid credentials"))
                .when(authService).authenticate("john.doe", "wrongPassword");

        assertThrows(IllegalArgumentException.class, () -> authAspect.enforceAuthentication(joinPoint));

        verify(joinPoint, never()).proceed();
    }
}
