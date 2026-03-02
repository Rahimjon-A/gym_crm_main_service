package epam.com.gym.crm.dao.impl;

import epam.com.gym.crm.model.Trainee;
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
class TraineeDaoImplTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<Trainee> typedQuery;

    @InjectMocks
    private TraineeDaoImpl traineeDao;

    private Trainee testTrainee;

    @BeforeEach
    void setUp() {
        testTrainee = new Trainee();
        testTrainee.setId(1L);
        testTrainee.setUsername("john.doe");
        testTrainee.setFirstName("John");

        traineeDao.setEntityManager(entityManager);
    }

    @Test
    void findByUsername_shouldReturnTrainee_whenFound() {
        String username = "john.doe";
        when(entityManager.createQuery(anyString(), eq(Trainee.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter(anyString(), any())).thenReturn(typedQuery);
        when(typedQuery.getResultStream()).thenReturn(Stream.of(testTrainee));

        Optional<Trainee> result = traineeDao.findByUsername(username);

        assertTrue(result.isPresent());
        assertEquals(username, result.get().getUsername());
        verify(entityManager).createQuery(contains("WHERE t.username = :username"), eq(Trainee.class));
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
    void delete_shouldRemove_whenEntityExists() {
        when(entityManager.find(Trainee.class, 1L)).thenReturn(testTrainee);

        traineeDao.delete(1L);

        verify(entityManager).remove(testTrainee);
    }
}
