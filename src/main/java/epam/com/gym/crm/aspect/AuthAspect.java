package epam.com.gym.crm.aspect;

import epam.com.gym.crm.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
        if (args.length < 2) {
            throw new SecurityException("Username and password must be provided");
        }
        if (!(args[0] instanceof String username) ||
                !(args[1] instanceof String password)) {
            throw new SecurityException("First two parameters must be username and password");
        }
        authService.authenticate(username, password);
        return joinPoint.proceed();
    }
}
