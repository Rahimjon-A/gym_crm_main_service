package epam.com.gym.crm.dao.impl;

import epam.com.gym.crm.model.*;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestPropertySource(properties = "spring.sql.init.mode=never")
@Import(TraineeDaoImpl.class)
class TraineeDaoImplTest {

    @Autowired
    private TraineeDaoImpl traineeDao;

    @Autowired
    private EntityManager entityManager;

    private Trainee savedTrainee;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setUsername("john.doe");
        user.setPassword("password123");
        user.setIsActive(true);

        Trainee trainee = new Trainee();
        trainee.setUser(user);
        trainee.setDateOfBirth(new Date());
        trainee.setAddress("123 Main St");

        savedTrainee = traineeDao.create(trainee);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void findByUsername_shouldReturnTrainee_whenUsernameExists() {
        Optional<Trainee> result = traineeDao.findByUsername("john.doe");

        assertTrue(result.isPresent());
        assertEquals("john.doe", result.get().getUser().getUsername());
        assertEquals("123 Main St", result.get().getAddress());
    }

    @Test
    void findByUsername_shouldReturnEmpty_whenUsernameDoesNotExist() {
        Optional<Trainee> result = traineeDao.findByUsername("unknown.user");

        assertTrue(result.isEmpty());
    }

    @Test
    void getUnassignedTrainers_shouldReturnTrainersNotAssignedToTrainee() {
        TrainingType type = new TrainingType();
        type.setTrainingTypeName("YOGA");
        entityManager.persist(type);

        User user1 = new User();
        user1.setFirstName("Assigned");
        user1.setLastName("Trainer");
        user1.setUsername("assigned.trainer");
        user1.setPassword("pass");
        user1.setIsActive(true);
        Trainer assignedTrainer = new Trainer();
        assignedTrainer.setUser(user1);
        assignedTrainer.setSpecialization(type);
        entityManager.persist(assignedTrainer);

        User user2 = new User();
        user2.setFirstName("Unassigned");
        user2.setLastName("Trainer");
        user2.setUsername("unassigned.trainer");
        user2.setPassword("pass");
        user2.setIsActive(true);
        Trainer unassignedTrainer = new Trainer();
        unassignedTrainer.setUser(user2);
        unassignedTrainer.setSpecialization(type);
        entityManager.persist(unassignedTrainer);

        Training training = new Training();
        training.setTrainee(savedTrainee);
        training.setTrainer(assignedTrainer);
        training.setTrainingType(type);
        training.setTrainingName("Morning Yoga");
        training.setTrainingDate(new Date());
        training.setTrainingDuration(60.0);
        entityManager.persist(training);

        entityManager.flush();
        entityManager.clear();

        List<Trainer> unassignedTrainers = traineeDao.getUnassignedTrainers("john.doe");

        assertEquals(1, unassignedTrainers.size());
        assertEquals("unassigned.trainer", unassignedTrainers.get(0).getUser().getUsername());
    }

    @Test
    void findById_shouldReturnTrainee_whenIdExists() {
        Optional<Trainee> result = traineeDao.findById(savedTrainee.getId());

        assertTrue(result.isPresent());
        assertEquals("john.doe", result.get().getUser().getUsername());
    }

    @Test
    void update_shouldModifyTrainee() {
        Trainee existing = traineeDao.findById(savedTrainee.getId()).get();

        existing.setAddress("456 Updated Ave");
        existing.getUser().setFirstName("Johnny");

        traineeDao.update(existing);
        entityManager.flush();
        entityManager.clear();

        Trainee updated = traineeDao.findById(savedTrainee.getId()).get();
        assertEquals("456 Updated Ave", updated.getAddress());
        assertEquals("Johnny", updated.getUser().getFirstName());
    }

    @Test
    void findAll_shouldReturnAllTrainees() {
        List<Trainee> result = traineeDao.findAll();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("john.doe", result.get(0).getUser().getUsername());
    }

    @Test
    void delete_shouldRemoveTrainee_whenIdExists() {
        Long id = savedTrainee.getId();

        traineeDao.delete(id);
        entityManager.flush();
        entityManager.clear();

        Optional<Trainee> result = traineeDao.findById(id);
        assertTrue(result.isEmpty());
    }
}