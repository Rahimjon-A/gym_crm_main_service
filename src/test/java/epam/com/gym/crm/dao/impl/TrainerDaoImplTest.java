package epam.com.gym.crm.dao.impl;

import epam.com.gym.crm.model.Trainer;
import epam.com.gym.crm.model.TrainingType;
import epam.com.gym.crm.model.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@TestPropertySource(properties = "spring.sql.init.mode=never")
@Import(TrainerDaoImpl.class)
class TrainerDaoImplTest {

    @Autowired
    private TrainerDaoImpl trainerDao;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Smith");
        user.setUsername("john.smith");
        user.setPassword("password123");
        user.setIsActive(true);
        entityManager.persist(user);

        TrainingType type = new TrainingType();
        type.setTrainingTypeName("FITNESS");
        entityManager.persist(type);

        Trainer trainer = new Trainer();
        trainer.setUser(user);
        trainer.setSpecialization(type);
        entityManager.persist(trainer);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void findByUsername_shouldReturnTrainer_whenUsernameExists() {
        Optional<Trainer> result = trainerDao.findByUsername("john.smith");

        assertTrue(result.isPresent());
        assertEquals("john.smith", result.get().getUser().getUsername());
        assertEquals("FITNESS", result.get().getSpecialization().getTrainingTypeName());
    }

    @Test
    void findByUsername_shouldReturnEmpty_whenUsernameDoesNotExist() {
        Optional<Trainer> result = trainerDao.findByUsername("unknown.user");

        assertTrue(result.isEmpty());
    }
}
