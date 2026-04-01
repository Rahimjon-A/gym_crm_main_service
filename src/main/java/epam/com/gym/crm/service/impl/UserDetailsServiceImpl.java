package epam.com.gym.crm.service.impl;

import epam.com.gym.crm.dao.UserDAO;
import epam.com.gym.crm.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserDAO<User> userDAO;

    @Autowired
    public void setUserDAO(UserDAO<User> userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Loading user by username: {}", username);

        return userDAO.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("User not found in security context: {}", username);
                    return new UsernameNotFoundException("User not found: " + username);
                });
    }
}
