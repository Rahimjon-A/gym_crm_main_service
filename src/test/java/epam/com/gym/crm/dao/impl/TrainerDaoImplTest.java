package epam.com.gym.crm.dao.impl;

import epam.com.gym.crm.model.Trainer;
import jakarta.persistence.EntityManager;
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

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<Trainer> typedQuery;

    @InjectMocks
    private TrainerDaoImpl trainerDao;

    private Trainer mockTrainer;

    @BeforeEach
    void setUp() {
        mockTrainer = new Trainer();
        mockTrainer.setId(10L);
        mockTrainer.setUsername("john.smith");
        mockTrainer.setFirstName("John");

        // Manually inject mock because AbstractBaseDAO uses @PersistenceContext
        trainerDao.setEntityManager(entityManager);
    }

    @Test
    void findByUsername_shouldReturnTrainer_whenFound() {
        String targetUser = "john.smith";
        when(entityManager.createQuery(anyString(), eq(Trainer.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter(anyString(), any())).thenReturn(typedQuery);
        when(typedQuery.getResultStream()).thenReturn(Stream.of(mockTrainer));

        Optional<Trainer> result = trainerDao.findByUsername(targetUser);

        assertTrue(result.isPresent());
        assertEquals(targetUser, result.get().getUsername());

        verify(entityManager).createQuery(contains("t.username = :username"), eq(Trainer.class));
        verify(typedQuery).setParameter("username", targetUser);
    }

    @Test
    void findByUsername_shouldReturnEmpty_whenNotFound() {
        when(entityManager.createQuery(anyString(), eq(Trainer.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter(anyString(), any())).thenReturn(typedQuery);
        when(typedQuery.getResultStream()).thenReturn(Stream.empty());

        Optional<Trainer> result = trainerDao.findByUsername("ghost.user");

        assertTrue(result.isEmpty());
    }

    @Test
    void getUnassignedTrainers_shouldCallQueryWithCorrectParameters() {
        String traineeUsername = "jane.doe";
        List<Trainer> expectedList = List.of(mockTrainer);

        when(entityManager.createQuery(anyString(), eq(Trainer.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter(anyString(), any())).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(expectedList);

        List<Trainer> result = trainerDao.getUnassignedTrainers(traineeUsername);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("john.smith", result.get(0).getUsername());

        verify(entityManager).createQuery(contains("WHERE NOT EXISTS"), eq(Trainer.class));
        verify(typedQuery).setParameter("username", traineeUsername);
    }
}
