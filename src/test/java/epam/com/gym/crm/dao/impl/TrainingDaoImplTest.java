package epam.com.gym.crm.dao.impl;

import epam.com.gym.crm.dao.filter.TraineeTrainingFilter;
import epam.com.gym.crm.model.Training;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingDaoImplTest {

    @Mock
    private EntityManager entityManager;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private CriteriaBuilder cb;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private CriteriaQuery<Training> cq;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Root<Training> root;

    @Mock
    private TypedQuery<Training> typedQuery;

    @InjectMocks
    private TrainingDaoImpl trainingDao;

    @BeforeEach
    void setUp() {
        when(entityManager.getCriteriaBuilder()).thenReturn(cb);
        when(cb.createQuery(Training.class)).thenReturn(cq);
        when(cq.from(Training.class)).thenReturn(root);
        when(entityManager.createQuery(cq)).thenReturn(typedQuery);
    }

    @Test
    void findTraineeTrainingsByCriteria_shouldBuildCorrectPredicates() {
        TraineeTrainingFilter filter = new TraineeTrainingFilter();
        filter.setTraineeName("john.doe");
        filter.setFromDate(new Date());

        List<Training> expectedResult = List.of(new Training());
        when(typedQuery.getResultList()).thenReturn(expectedResult);

        List<Training> actualResult = trainingDao.findTraineeTrainingsByCriteria(filter);

        assertEquals(expectedResult, actualResult);

        verify(cb).equal(root.get("trainee").get("username"), "john.doe");

        verify(cb).greaterThanOrEqualTo(root.get("trainingDate"), filter.getFromDate());

        verify(cq).where(any(Predicate[].class));
        verify(entityManager).createQuery(cq);
    }

    @Test
    void findTraineeTrainingsByCriteria_shouldHandleNullOptionals() {
        TraineeTrainingFilter filter = new TraineeTrainingFilter();
        filter.setTraineeName("john.doe");

        trainingDao.findTraineeTrainingsByCriteria(filter);

        verify(cb, times(1)).equal(any(Expression.class), eq("john.doe"));
        verify(cb, never()).greaterThanOrEqualTo(any(), any(Date.class));
    }
}
