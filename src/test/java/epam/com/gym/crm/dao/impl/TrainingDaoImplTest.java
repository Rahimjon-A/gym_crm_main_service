package epam.com.gym.crm.dao.impl;

import epam.com.gym.crm.dao.filter.TraineeTrainingFilter;
import epam.com.gym.crm.dao.filter.TrainerTrainingFilter;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingDaoImplTest {
    private static final String TARGET_USERNAME = "john.doe";
    private static final String TARGET_FIRST_NAME = "Jane";
    private static final String TARGET_TRAINING_TYPE = "YOGA";

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

    private Date now;

    @BeforeEach
    void setUp() {
        now = new Date();

        when(entityManager.getCriteriaBuilder()).thenReturn(cb);
        when(cb.createQuery(Training.class)).thenReturn(cq);
        when(cq.from(Training.class)).thenReturn(root);
        when(entityManager.createQuery(cq)).thenReturn(typedQuery);
    }

    @Test
    void findTraineeTrainingsByCriteria_shouldHitAllBranches_whenFullyPopulated() {
        TraineeTrainingFilter filter = new TraineeTrainingFilter();
        filter.setUsername(TARGET_USERNAME);
        filter.setTrainerName(TARGET_FIRST_NAME);
        filter.setTrainingTypeName(TARGET_TRAINING_TYPE);
        filter.setFromDate(now);
        filter.setToDate(now);

        List<Training> expectedResult = List.of(new Training());
        when(typedQuery.getResultList()).thenReturn(expectedResult);

        List<Training> actualResult = trainingDao.findTraineeTrainingsByCriteria(filter);

        assertEquals(expectedResult, actualResult);

        verify(cb).equal(root.get("trainee").get("username"), TARGET_USERNAME);
        verify(cb).equal(root.get("trainer").get("firstName"), TARGET_FIRST_NAME);
        verify(cb).equal(root.get("trainingType").get("trainingTypeName"), TARGET_TRAINING_TYPE);
        verify(cb).greaterThanOrEqualTo(root.get("trainingDate"), filter.getFromDate());
        verify(cb).lessThanOrEqualTo(root.get("trainingDate"), filter.getToDate());
    }

    @Test
    void findTrainerTrainingsByCriteria_shouldHitAllBranches_whenFullyPopulated() {
        TrainerTrainingFilter filter = new TrainerTrainingFilter();
        filter.setUsername(TARGET_USERNAME);
        filter.setTraineeName(TARGET_FIRST_NAME);
        filter.setFromDate(now);
        filter.setToDate(now);

        List<Training> expectedResult = List.of(new Training());
        when(typedQuery.getResultList()).thenReturn(expectedResult);

        List<Training> actualResult = trainingDao.findTrainerTrainingsByCriteria(filter);

        assertEquals(expectedResult, actualResult);

        verify(cb).equal(root.get("trainer").get("username"), TARGET_USERNAME);
        verify(cb).equal(root.get("trainee").get("firstName"), TARGET_FIRST_NAME);
        verify(cb).greaterThanOrEqualTo(root.get("trainingDate"), filter.getFromDate());
        verify(cb).lessThanOrEqualTo(root.get("trainingDate"), filter.getToDate());
    }

    @Test
    void bothMethods_shouldGracefullySkipOptionalPredicates_whenMinimalFilterProvided() {
        TraineeTrainingFilter traineeFilter = new TraineeTrainingFilter();
        traineeFilter.setUsername(TARGET_USERNAME);

        TrainerTrainingFilter trainerFilter = new TrainerTrainingFilter();
        trainerFilter.setUsername(TARGET_USERNAME);

        trainingDao.findTraineeTrainingsByCriteria(traineeFilter);
        trainingDao.findTrainerTrainingsByCriteria(trainerFilter);

        verify(cb, times(1)).equal(root.get("trainee").get("username"), TARGET_USERNAME);
        verify(cb, times(1)).equal(root.get("trainer").get("username"), TARGET_USERNAME);
    }
}
