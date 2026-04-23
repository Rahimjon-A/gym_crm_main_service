package epam.com.gym.crm.service.impl;

import epam.com.gym.crm.dao.UserDAO;
import epam.com.gym.crm.model.User;
import epam.com.gym.crm.service.BruteForceProtectionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    private static final String USERNAME = "john.doe";

    @Mock
    private UserDAO<User> userDAO;

    @Mock
    private User user;

    @Mock
    private BruteForceProtectionService bruteForceProtectionService;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void loadUserByUsername_shouldReturnUserDetails_whenUserExists() {
        when(userDAO.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(bruteForceProtectionService.isBlocked(USERNAME)).thenReturn(false);

        UserDetails result = userDetailsService.loadUserByUsername(USERNAME);

        assertNotNull(result);
        assertEquals(user, result);
        verify(userDAO).findByUsername(USERNAME);
    }

    @Test
    void loadUserByUsername_shouldThrowUsernameNotFoundException_whenUserNotFound() {
        when(userDAO.findByUsername(USERNAME)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(USERNAME));

        verify(userDAO).findByUsername(USERNAME);
    }
}
