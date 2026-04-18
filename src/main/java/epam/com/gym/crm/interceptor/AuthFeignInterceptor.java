package epam.com.gym.crm.interceptor;

import epam.com.gym.crm.service.JwtService;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthFeignInterceptor implements RequestInterceptor {
    public static final String AUTHORIZATION = "Authorization";

    private JwtService jwtService;

    @Autowired
    public void setJwtService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public void apply(RequestTemplate template) {
        String bearer_token = jwtService.generateServiceToken();
        template.header(AUTHORIZATION, bearer_token);

        log.info("Added Authorization header for Feign request");
    }
}
