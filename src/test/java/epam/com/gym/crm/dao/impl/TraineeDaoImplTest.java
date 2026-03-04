package epam.com.gym.crm.dao.impl;

import epam.com.gym.crm.model.Trainee;
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
class TraineeDaoImplTest {
    private static final String TARGET_USERNAME = "john.doe";
    private static final String EXPECTED_QUERY_FRAGMENT = "WHERE e.username = :username";
    private static final String EXPECTED_DELETE_QUERY = "DELETE FROM Trainee e WHERE e.id = :id";
    private static final String PARAM_ID = "id";
    private static final Long TARGET_ID = 1L;

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<Trainee> typedQuery;

    @Mock
    private EntityManagerFactory entityManagerFactory;

    @Mock
    private PersistenceUnitUtil persistenceUnitUtil;

    @InjectMocks
    private TraineeDaoImpl traineeDao;

    private Trainee testTrainee;

    @BeforeEach
    void setUp() {
        testTrainee = new Trainee();
        testTrainee.setId(1L);
        testTrainee.setUsername("john.doe");
        testTrainee.setFirstName("John");

        lenient().when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);
        lenient().when(entityManagerFactory.getPersistenceUnitUtil()).thenReturn(persistenceUnitUtil);

        lenient().when(persistenceUnitUtil.getIdentifier(any())).thenReturn(1L);

        traineeDao.setEntityManager(entityManager);
    }

    @Test
    void findByUsername_shouldReturnTrainee_whenFound() {
        when(entityManager.createQuery(anyString(), eq(Trainee.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter(anyString(), any())).thenReturn(typedQuery);
        when(typedQuery.getResultStream()).thenReturn(Stream.of(testTrainee));

        Optional<Trainee> result = traineeDao.findByUsername(TARGET_USERNAME);

        assertTrue(result.isPresent());
        assertEquals(TARGET_USERNAME, result.get().getUsername());

        verify(entityManager).createQuery(contains(EXPECTED_QUERY_FRAGMENT), eq(Trainee.class));
    }

    @Test
    void create_shouldCallPersist() {
        Trainee result = traineeDao.create(testTrainee);

        assertNotNull(result);
        verify(entityManager, times(1)).persist(testTrainee);
    }

    @Test
    void findById_shouldReturnOptional_whenFound() {
        when(entityManager.find(Trainee.class, 1L)).thenReturn(testTrainee);

        Optional<Trainee> result = traineeDao.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void update_shouldCallMerge() {
        when(entityManager.merge(testTrainee)).thenReturn(testTrainee);

        Trainee result = traineeDao.update(testTrainee);

        verify(entityManager).merge(testTrainee);
        assertEquals(testTrainee, result);
    }

    @Test
    void findAll_shouldReturnList() {
        List<Trainee> list = List.of(testTrainee);
        when(entityManager.createQuery(anyString(), eq(Trainee.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(list);

        List<Trainee> result = traineeDao.findAll();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void delete_shouldExecuteDeleteQuery() {
        doReturn(typedQuery).when(entityManager).createQuery(anyString());
        when(typedQuery.setParameter(anyString(), any())).thenReturn(typedQuery);
        when(typedQuery.executeUpdate()).thenReturn(1);

        traineeDao.delete(TARGET_ID);

        verify(entityManager).createQuery(contains(EXPECTED_DELETE_QUERY));
        verify(typedQuery).setParameter(PARAM_ID, TARGET_ID);
        verify(typedQuery).executeUpdate();

        verify(entityManager, never()).find(any(), any());
        verify(entityManager, never()).remove(any());
    }
}
