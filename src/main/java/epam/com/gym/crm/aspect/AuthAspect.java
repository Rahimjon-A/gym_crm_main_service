package epam.com.gym.crm.aspect;

import epam.com.gym.crm.model.common.Credentials;
import epam.com.gym.crm.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class AuthAspect {

    private AuthService authService;

    @Autowired
    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }

    @Around("@annotation(epam.com.gym.crm.aspect.RequireAuth)")
    public Object enforceAuthentication(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();

        Credentials credentials = Arrays.stream(args)
                .filter(arg -> arg instanceof Credentials)
                .map(arg -> (Credentials) arg)
                .findFirst()
                .orElseThrow(() -> new SecurityException("Credentials must be provided for this operation"));

        authService.authenticate(credentials);

        return joinPoint.proceed();
    }
}
