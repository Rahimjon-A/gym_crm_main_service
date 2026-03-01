package epam.com.gym.crm.dao.impl;

import epam.com.gym.crm.dto.TraineeTrainingFilter;
import epam.com.gym.crm.dto.TrainerTrainingFilter;
import epam.com.gym.crm.model.*;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
@TestPropertySource(properties = "spring.sql.init.mode=never")
@Import(TrainingDaoImpl.class)
class TrainingDaoImplTest {

    @Autowired
    private TrainingDaoImpl trainingDao;

    @Autowired
    private EntityManager entityManager;

    private Date pastDate;
    private Date futureDate;

    @BeforeEach
    void setUp() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -10);
        pastDate = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 20);
        futureDate = cal.getTime();

        User traineeUser = new User();
        traineeUser.setFirstName("John");
        traineeUser.setLastName("Doe");
        traineeUser.setUsername("john.doe");
        traineeUser.setPassword("pass");
        traineeUser.setIsActive(true);
        entityManager.persist(traineeUser);

        Trainee trainee = new Trainee();
        trainee.setUser(traineeUser);
        trainee.setAddress("123 Main St, New York");
        entityManager.persist(trainee);

        User trainerUser = new User();
        trainerUser.setFirstName("Jane");
        trainerUser.setLastName("Smith");
        trainerUser.setUsername("jane.smith");
        trainerUser.setPassword("pass");
        trainerUser.setIsActive(true);
        entityManager.persist(trainerUser);

        TrainingType type = new TrainingType();
        type.setTrainingTypeName("YOGA");
        entityManager.persist(type);

        Trainer trainer = new Trainer();
        trainer.setUser(trainerUser);
        trainer.setSpecialization(type);
        entityManager.persist(trainer);

        Training training = new Training();
        training.setTrainee(trainee);
        training.setTrainer(trainer);
        training.setTrainingType(type);
        training.setTrainingName("Morning Yoga Session");
        training.setTrainingDate(new Date());
        training.setTrainingDuration(60.0);
        entityManager.persist(training);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void findTraineeTrainingsByCriteria_shouldHitAllFiltersAndReturnMatch() {
        TraineeTrainingFilter filter = new TraineeTrainingFilter();
        filter.setTraineeUsername("john.doe");
        filter.setFromDate(pastDate);
        filter.setToDate(futureDate);
        filter.setTrainerName("jane.smith");
        filter.setTrainingTypeName("YOGA");
        filter.setDuration(60.0);
        filter.setTrainingName("Morning");

        List<Training> results = trainingDao.findTraineeTrainingsByCriteria(filter);

        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals("Morning Yoga Session", results.get(0).getTrainingName());
    }

    @Test
    void findTrainerTrainingsByCriteria_shouldHitAllFiltersAndReturnMatch() {
        TrainerTrainingFilter filter = new TrainerTrainingFilter();
        filter.setTrainerUsername("jane.smith");
        filter.setFromDate(pastDate);
        filter.setToDate(futureDate);
        filter.setTraineeName("john.doe");
        filter.setTraineeAddress("New York");
        filter.setDuration(60.0);
        filter.setTrainingName("Morning Yoga Session");

        List<Training> results = trainingDao.findTrainerTrainingsByCriteria(filter);

        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals("Morning Yoga Session", results.get(0).getTrainingName());
    }
}
