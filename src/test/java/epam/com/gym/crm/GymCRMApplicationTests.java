package epam.com.gym.crm;

import epam.com.gym.crm.service.UserService;
import epam.com.gym.crm.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
@ActiveProfiles("test")
class GymCRMApplicationTests {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private UserService userService;

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Test
    void applicationShouldBeProperlyConfigured() {
        assertTrue(context.containsBean("userServiceImpl"), "UserService should be registered");
        assertThat(userService).isInstanceOf(UserServiceImpl.class);

        assertThat(dbUrl).contains("jdbc:h2");

        assertTrue(AopUtils.isAopProxy(userService), "UserService should be a proxy (for @Transactional/@RequireAuth)");
    }
}
