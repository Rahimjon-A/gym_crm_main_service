package epam.com.gym.crm.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    public static final String SCHEME_NAME = "basicAuth";
    private static final String NAME = "Rakhimjon Abdurakhimov";
    private static final String EMAIL = "rakhimjon_abdurakhimov@epam.com";
    private static final String TITLE = "Gym CRM API";
    private static final String VERSION = "1.0";
    private static final String DESC = "REST API documentation for the EPAM Gym CRM project.";
    private static final String SCHEME = "basic";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(TITLE)
                        .version(VERSION)
                        .description(DESC)
                        .contact(new Contact()
                                .name(NAME)
                                .email(EMAIL)))
                .addSecurityItem(new SecurityRequirement().addList(SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(SCHEME_NAME, new SecurityScheme()
                                .name(SCHEME_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme(SCHEME)));
    }
}
