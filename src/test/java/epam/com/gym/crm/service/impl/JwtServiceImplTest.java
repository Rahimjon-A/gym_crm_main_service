package epam.com.gym.crm.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtServiceImplTest {

    private static final String TEST_SECRET =
            "dGVzdC1zZWNyZXQta2V5LXRoYXQtaXMtbG9uZy1lbm91Z2gtZm9yLUhTMjU2";
    private static final long TEST_EXPIRATION = 86400000L;
    private static final String USERNAME = "john.doe";

    @InjectMocks
    private JwtServiceImpl jwtService;

    @Mock
    private UserDetails userDetails;

    @BeforeEach
    void setUp() throws Exception {
        setField(jwtService, "secretKey", TEST_SECRET);
        setField(jwtService, "jwtExpiration", TEST_EXPIRATION);
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    void generateToken_shouldReturnNonNullToken() {
        when(userDetails.getUsername()).thenReturn(USERNAME);

        String token = jwtService.generateToken(userDetails);

        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void extractUsername_shouldReturnCorrectUsername() {
        when(userDetails.getUsername()).thenReturn(USERNAME);
        String token = jwtService.generateToken(userDetails);

        String extracted = jwtService.extractUsername(token);

        assertEquals(USERNAME, extracted);
    }

    @Test
    void isTokenValid_shouldReturnTrue_whenTokenIsValid() {
        when(userDetails.getUsername()).thenReturn(USERNAME);
        String token = jwtService.generateToken(userDetails);

        assertTrue(jwtService.isTokenValid(token, userDetails));
    }

    @Test
    void isTokenValid_shouldReturnFalse_whenUsernameDoesNotMatch() {
        when(userDetails.getUsername()).thenReturn(USERNAME);
        String token = jwtService.generateToken(userDetails);

        UserDetails otherUser = mock(UserDetails.class);
        when(otherUser.getUsername()).thenReturn("other.user");

        assertFalse(jwtService.isTokenValid(token, otherUser));
    }

    @Test
    void isTokenValid_shouldReturnFalse_whenTokenIsExpired() throws Exception {
        setField(jwtService, "jwtExpiration", -100L);
        String expiredToken = jwtService.generateToken(userDetails);

        assertFalse(jwtService.isTokenValid(expiredToken, userDetails));
    }
}
