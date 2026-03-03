package epam.com.gym.crm.dao.impl;

import epam.com.gym.crm.dao.TrainingDAO;
import epam.com.gym.crm.dao.filter.BaseTrainingFilter;
import epam.com.gym.crm.dao.filter.TraineeTrainingFilter;
import epam.com.gym.crm.dao.filter.TrainerTrainingFilter;
import epam.com.gym.crm.model.Training;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class TrainingDaoImpl extends AbstractBaseDAO<Training> implements TrainingDAO {
    private static final String FIELD_TRAINEE = "trainee";
    private static final String FIELD_TRAINER = "trainer";
    private static final String FIELD_USERNAME = "username";
    private static final String FIELD_TRAINING_TYPE = "trainingType";
    private static final String FIELD_TRAINING_TYPE_NAME = "trainingTypeName";
    private static final String FIELD_TRAINING_DATE = "trainingDate";

    public TrainingDaoImpl() {
        super(Training.class);
    }

    @Override
    public List<Training> findTraineeTrainingsByCriteria(TraineeTrainingFilter request) {
        log.debug("Searching trainings for Trainee: {}", request.getTraineeUsername());

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Training> cq = cb.createQuery(Training.class);
        Root<Training> root = cq.from(Training.class);
        List<Predicate> predicates = new ArrayList<>();

        addUsernamePredicate(cb, root, predicates, FIELD_TRAINEE, request.getTraineeUsername());
        addUsernamePredicate(cb, root, predicates, FIELD_TRAINER, request.getTrainerName());

        if (request.getTrainingTypeName() != null) {
            predicates.add(cb.equal(root.get(FIELD_TRAINING_TYPE).get(FIELD_TRAINING_TYPE_NAME), request.getTrainingTypeName()));
        }

        addCommonPredicates(cb, root, predicates, request);

        return executeQuery(cq, predicates);
    }

    @Override
    public List<Training> findTrainerTrainingsByCriteria(TrainerTrainingFilter request) {
        log.debug("Searching trainings for Trainer: {}", request.getTrainerUsername());

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Training> cq = cb.createQuery(Training.class);
        Root<Training> root = cq.from(Training.class);
        List<Predicate> predicates = new ArrayList<>();

        addUsernamePredicate(cb, root, predicates, FIELD_TRAINER, request.getTrainerUsername());
        addUsernamePredicate(cb, root, predicates, FIELD_TRAINEE, request.getTraineeName());

        addCommonPredicates(cb, root, predicates, request);

        return executeQuery(cq, predicates);
    }

    private void addUsernamePredicate(CriteriaBuilder cb, Root<Training> root,
                                      List<Predicate> predicates, String relationField, String username) {
        if (username != null && !username.isBlank()) {
            predicates.add(cb.equal(root.get(relationField).get(FIELD_USERNAME), username));
        }
    }

    private void addCommonPredicates(CriteriaBuilder cb, Root<Training> root,
                                     List<Predicate> predicates, BaseTrainingFilter filter) {
        if (filter.getFromDate() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get(FIELD_TRAINING_DATE), filter.getFromDate()));
        }
        if (filter.getToDate() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get(FIELD_TRAINING_DATE), filter.getToDate()));
        }
    }

    private List<Training> executeQuery(CriteriaQuery<Training> cq, List<Predicate> predicates) {
        cq.where(predicates.toArray(new Predicate[0]));
        return getEntityManager().createQuery(cq).getResultList();
    }
}