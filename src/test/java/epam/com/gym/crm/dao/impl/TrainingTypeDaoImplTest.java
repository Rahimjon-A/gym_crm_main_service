package epam.com.gym.crm.dao.impl;

import epam.com.gym.crm.model.TrainingType;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingTypeDaoImplTest {
    private static final Long TYPE_ID = 1L;
    private static final String TYPE_NAME = "YOGA";
    private static final String PARAM_NAME = "name";
    private static final String FIND_BY_NAME_QUERY = "SELECT tt FROM TrainingType tt WHERE tt.trainingTypeName = :name";
    private static final String FIND_ALL_QUERY = "FROM TrainingType";
    private static final String COUNT_QUERY = "SELECT count(t) FROM TrainingType t";

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<TrainingType> typedQuery;

    @Mock
    private TypedQuery<Long> countQuery;

    @InjectMocks
    private TrainingTypeDaoImpl trainingTypeDao;

    private TrainingType mockType;

    @BeforeEach
    void setUp() {
        mockType = new TrainingType();
        mockType.setId(TYPE_ID);
        mockType.setTrainingTypeName(TYPE_NAME);

        trainingTypeDao.setEntityManager(entityManager);
    }

    @Test
    void findById_shouldReturnOptional_whenFound() {
        when(entityManager.find(TrainingType.class, TYPE_ID)).thenReturn(mockType);

        Optional<TrainingType> result = trainingTypeDao.findById(TYPE_ID);

        assertTrue(result.isPresent());
        assertEquals(TYPE_ID, result.get().getId());
        assertEquals(TYPE_NAME, result.get().getTrainingTypeName());
        
        verify(entityManager, times(1)).find(TrainingType.class, TYPE_ID);
    }

    @Test
    void findById_shouldReturnEmpty_whenNotFound() {
        when(entityManager.find(TrainingType.class, TYPE_ID)).thenReturn(null);

        Optional<TrainingType> result = trainingTypeDao.findById(TYPE_ID);

        assertTrue(result.isEmpty());
        verify(entityManager, times(1)).find(TrainingType.class, TYPE_ID);
    }


    @Test
    void findByName_shouldReturnEmpty_whenNameIsNull() {
        Optional<TrainingType> result = trainingTypeDao.findByName(null);

        assertTrue(result.isEmpty());
        verifyNoInteractions(entityManager);
    }

    @Test
    void findByName_shouldReturnOptional_whenFound() {
        when(entityManager.createQuery(FIND_BY_NAME_QUERY, TrainingType.class)).thenReturn(typedQuery);
        when(typedQuery.setParameter(PARAM_NAME, TYPE_NAME)).thenReturn(typedQuery);
        when(typedQuery.getResultStream()).thenReturn(Stream.of(mockType));

        Optional<TrainingType> result = trainingTypeDao.findByName(TYPE_NAME);

        assertTrue(result.isPresent());
        assertEquals(TYPE_NAME, result.get().getTrainingTypeName());

        verify(entityManager).createQuery(FIND_BY_NAME_QUERY, TrainingType.class);
        verify(typedQuery).setParameter(PARAM_NAME, TYPE_NAME);
    }

    @Test
    void findByName_shouldReturnEmpty_whenNotFound() {
        when(entityManager.createQuery(FIND_BY_NAME_QUERY, TrainingType.class)).thenReturn(typedQuery);
        when(typedQuery.setParameter(PARAM_NAME, TYPE_NAME)).thenReturn(typedQuery);
        when(typedQuery.getResultStream()).thenReturn(Stream.empty());

        Optional<TrainingType> result = trainingTypeDao.findByName(TYPE_NAME);

        assertTrue(result.isEmpty());
    }

    @Test
    void findAll_shouldReturnList() {
        List<TrainingType> expectedList = List.of(mockType);

        when(entityManager.createQuery(FIND_ALL_QUERY, TrainingType.class)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(expectedList);

        List<TrainingType> result = trainingTypeDao.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(expectedList, result);

        verify(entityManager).createQuery(FIND_ALL_QUERY, TrainingType.class);
        verify(typedQuery).getResultList();
    }

    @Test
    void count_shouldReturnTotalRecords() {
        Long expectedCount = 5L;

        when(entityManager.createQuery(COUNT_QUERY, Long.class)).thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(expectedCount);

        Long result = trainingTypeDao.count();

        assertNotNull(result);
        assertEquals(expectedCount, result);

        verify(entityManager).createQuery(COUNT_QUERY, Long.class);
        verify(countQuery).getSingleResult();
    }
}
