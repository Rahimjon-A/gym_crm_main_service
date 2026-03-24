package epam.com.gym.crm.dao.impl;

import epam.com.gym.crm.model.Trainer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnitUtil;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerDaoImplTest {
    private static final String TARGET_USERNAME = "john.smith";
    private static final String GHOST_USERNAME = "ghost.user";
    private static final String TRAINEE_USERNAME = "jane.doe";
    private static final String FIRST_NAME = "John";
    private static final String PARAM_USERNAME = "username";
    private static final String EXPECTED_FIND_BY_USERNAME_FRAGMENT = "WHERE e.username = :username";
    private static final String EXPECTED_UNASSIGNED_FRAGMENT = "AND NOT EXISTS";
    private static final Long TRAINER_ID = 10L;

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<Trainer> typedQuery;

    @Mock
    private EntityManagerFactory entityManagerFactory;

    @Mock
    private PersistenceUnitUtil persistenceUnitUtil;

    @InjectMocks
    private TrainerDaoImpl trainerDao;

    private Trainer mockTrainer;

    @BeforeEach
    void setUp() {
        mockTrainer = new Trainer();
        mockTrainer.setId(TRAINER_ID);
        mockTrainer.setUsername(TARGET_USERNAME);
        mockTrainer.setFirstName(FIRST_NAME);

        lenient().when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);
        lenient().when(entityManagerFactory.getPersistenceUnitUtil()).thenReturn(persistenceUnitUtil);
        lenient().when(persistenceUnitUtil.getIdentifier(any())).thenReturn(TRAINER_ID);

        trainerDao.setEntityManager(entityManager);
    }

    @Test
    void findByUsername_shouldReturnTrainer_whenFound() {
        when(entityManager.createQuery(anyString(), eq(Trainer.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter(anyString(), any())).thenReturn(typedQuery);
        when(typedQuery.getResultStream()).thenReturn(Stream.of(mockTrainer));

        Optional<Trainer> result = trainerDao.findByUsername(TARGET_USERNAME);

        assertTrue(result.isPresent());
        assertEquals(TARGET_USERNAME, result.get().getUsername());

        verify(entityManager).createQuery(contains(EXPECTED_FIND_BY_USERNAME_FRAGMENT), eq(Trainer.class));
        verify(typedQuery).setParameter(PARAM_USERNAME, TARGET_USERNAME);
    }

    @Test
    void findByUsername_shouldReturnEmpty_whenNotFound() {
        when(entityManager.createQuery(anyString(), eq(Trainer.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter(anyString(), any())).thenReturn(typedQuery);
        when(typedQuery.getResultStream()).thenReturn(Stream.empty());

        Optional<Trainer> result = trainerDao.findByUsername(GHOST_USERNAME);

        assertTrue(result.isEmpty());
    }

    @Test
    void getUnassignedTrainers_shouldCallQueryWithCorrectParameters() {
        List<Trainer> expectedList = List.of(mockTrainer);

        when(entityManager.createQuery(anyString(), eq(Trainer.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter(anyString(), any())).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(expectedList);

        List<Trainer> result = trainerDao.getUnassignedTrainers(TRAINEE_USERNAME);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(TARGET_USERNAME, result.get(0).getUsername());

        verify(entityManager).createQuery(contains(EXPECTED_UNASSIGNED_FRAGMENT), eq(Trainer.class));
        verify(typedQuery).setParameter(PARAM_USERNAME, TRAINEE_USERNAME);
    }
}
