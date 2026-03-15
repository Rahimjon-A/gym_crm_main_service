package epam.com.gym.crm.config;

import epam.com.gym.crm.auth.AuthenticationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    public static final String SECURE_API_PATTERN = "/api/v1/**";
    public static final String EXCLUDE_AUTH_PATTERN = "/api/v1/auth/**";
    public static final String EXCLUDE_SWAGGER_UI = "/swagger-ui/**";
    public static final String EXCLUDE_API_DOCS = "/v3/api-docs/**";

    private AuthenticationInterceptor authenticationInterceptor;

    @Autowired
    public void setAuthenticationInterceptor(AuthenticationInterceptor authenticationInterceptor) {
        this.authenticationInterceptor = authenticationInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor)
                .addPathPatterns(SECURE_API_PATTERN)
                .excludePathPatterns(
                        EXCLUDE_AUTH_PATTERN,
                        EXCLUDE_SWAGGER_UI,
                        EXCLUDE_API_DOCS
                );
    }
}
