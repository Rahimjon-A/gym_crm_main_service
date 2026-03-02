package epam.com.gym.crm.dao.impl;

import epam.com.gym.crm.dao.TrainingDAO;
import epam.com.gym.crm.filter.BaseTrainingFilter;
import epam.com.gym.crm.filter.TraineeTrainingFilter;
import epam.com.gym.crm.filter.TrainerTrainingFilter;
import epam.com.gym.crm.model.Training;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class TrainingDaoImpl extends AbstractBaseDAO<Training> implements TrainingDAO {

    public TrainingDaoImpl() {
        super(Training.class);
    }

    @Override
    public List<Training> findTraineeTrainingsByCriteria(TraineeTrainingFilter request) {
        log.debug("Searching trainings for Trainee: {}", request.getTraineeUsername());

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Training> cq = cb.createQuery(Training.class);
        Root<Training> training = cq.from(Training.class);

        List<Predicate> predicates = new ArrayList<>();

        predicates.add(cb.equal(training.get("trainee").get("username"), request.getTraineeUsername()));

        if (request.getTrainerName() != null) {
            predicates.add(cb.equal(training.get("trainer").get("username"), request.getTrainerName()));
        }
        if (request.getTrainingTypeName() != null) {
            predicates.add(cb.equal(training.get("trainingType").get("trainingTypeName"), request.getTrainingTypeName()));
        }

        addCommonPredicates(cb, training, predicates, request);

        return executeQuery(cq, predicates);
    }

    @Override
    public List<Training> findTrainerTrainingsByCriteria(TrainerTrainingFilter request) {
        log.debug("Searching trainings for Trainer: {}", request.getTrainerUsername());

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Training> cq = cb.createQuery(Training.class);
        Root<Training> training = cq.from(Training.class);

        List<Predicate> predicates = new ArrayList<>();

        predicates.add(cb.equal(training.get("trainer").get("username"), request.getTrainerUsername()));

        if (request.getTraineeName() != null) {
            predicates.add(cb.equal(training.get("trainee").get("username"), request.getTraineeName()));
        }
        if (request.getTraineeAddress() != null && !request.getTraineeAddress().isEmpty()) {
            predicates.add(cb.like(training.get("trainee").get("address"), "%" + request.getTraineeAddress() + "%"));
        }

        addCommonPredicates(cb, training, predicates, request);

        return executeQuery(cq, predicates);
    }

    private void addCommonPredicates(CriteriaBuilder cb, Root<Training> training,
                                     List<Predicate> predicates, BaseTrainingFilter filter) {
        if (filter.getFromDate() != null) {
            predicates.add(cb.greaterThanOrEqualTo(training.get("trainingDate"), filter.getFromDate()));
        }
        if (filter.getToDate() != null) {
            predicates.add(cb.lessThanOrEqualTo(training.get("trainingDate"), filter.getToDate()));
        }
        if (filter.getDuration() != null) {
            predicates.add(cb.equal(training.get("trainingDuration"), filter.getDuration()));
        }
        if (filter.getTrainingName() != null) {
            predicates.add(cb.like(training.get("trainingName"), "%" + filter.getTrainingName() + "%"));
        }
    }

    private List<Training> executeQuery(CriteriaQuery<Training> cq, List<Predicate> predicates) {
        cq.where(predicates.toArray(new Predicate[0]));
        return getEntityManager().createQuery(cq).getResultList();
    }
}
