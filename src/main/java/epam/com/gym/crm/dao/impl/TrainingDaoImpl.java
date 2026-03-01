package epam.com.gym.crm.dao.impl;

import epam.com.gym.crm.dao.TrainingDAO;
import epam.com.gym.crm.dto.TraineeTrainingFilter;
import epam.com.gym.crm.dto.TrainerTrainingFilter;
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
        log.info("Searching trainings for Trainee: {}", request.getTraineeUsername());
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Training> cq = cb.createQuery(Training.class);
        Root<Training> training = cq.from(Training.class);

        List<Predicate> predicates = new ArrayList<>();

        predicates.add(cb.equal(training.get("trainee").get("user").get("username"), request.getTraineeUsername()));

        if (request.getFromDate() != null) {
            predicates.add(cb.greaterThanOrEqualTo(training.get("trainingDate"), request.getFromDate()));
        }
        if (request.getToDate() != null) {
            predicates.add(cb.lessThanOrEqualTo(training.get("trainingDate"), request.getToDate()));
        }
        if (request.getTrainerName() != null) {
            predicates.add(cb.equal(training.get("trainer").get("user").get("username"), request.getTrainerName()));
        }
        if (request.getTrainingTypeName() != null) {
            predicates.add(cb.equal(training.get("trainingType").get("trainingTypeName"), request.getTrainingTypeName()));
        }
        if (request.getDuration() != null) {
            predicates.add(cb.equal(training.get("trainingDuration"), request.getDuration()));
        }
        if (request.getTrainingName() != null) {
            predicates.add(cb.like(training.get("trainingName"), "%" + request.getTrainingName() + "%"));
        }

        cq.where(cb.and(predicates.toArray(new Predicate[0])));
        return getEntityManager().createQuery(cq).getResultList();
    }

    @Override
    public List<Training> findTrainerTrainingsByCriteria(TrainerTrainingFilter request) {
        log.info("Searching trainings for Trainer: {}", request.getTrainerUsername());
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Training> cq = cb.createQuery(Training.class);
        Root<Training> training = cq.from(Training.class);

        List<Predicate> predicates = new ArrayList<>();

        predicates.add(cb.equal(training.get("trainer").get("user").get("username"), request.getTrainerUsername()));

        if (request.getFromDate() != null) {
            predicates.add(cb.greaterThanOrEqualTo(training.get("trainingDate"), request.getFromDate()));
        }
        if (request.getToDate() != null) {
            predicates.add(cb.lessThanOrEqualTo(training.get("trainingDate"), request.getToDate()));
        }
        if (request.getTraineeName() != null) {
            predicates.add(cb.equal(training.get("trainee").get("user").get("username"), request.getTraineeName()));
        }

        if (request.getTraineeAddress() != null && !request.getTraineeAddress().isEmpty()) {
            predicates.add(cb.like(training.get("trainee").get("address"), "%" + request.getTraineeAddress() + "%"));
        }

        if (request.getDuration() != null) {
            predicates.add(cb.equal(training.get("trainingDuration"), request.getDuration()));
        }
        if (request.getTrainingName() != null) {
            predicates.add(cb.equal(training.get("trainingName"), request.getTrainingName()));
        }

        cq.where(cb.and(predicates.toArray(new Predicate[0])));
        return getEntityManager().createQuery(cq).getResultList();
    }
}