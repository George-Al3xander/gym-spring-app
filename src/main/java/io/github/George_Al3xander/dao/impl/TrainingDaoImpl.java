package io.github.George_Al3xander.dao.impl;

import io.github.George_Al3xander.dao.TrainingDao;
import io.github.George_Al3xander.dto.filter.TrainingFilter;
import io.github.George_Al3xander.model.Training;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class TrainingDaoImpl implements TrainingDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Training save(Training entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public Optional<Training> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Training.class, id));
    }

    @Override
    public List<Training> findAll() {
        return entityManager
                .createQuery("SELECT t FROM Training t", Training.class)
                .getResultList();
    }

    @Override
    public void delete(Long id) {
        Training training = entityManager.getReference(Training.class, id);
        entityManager.remove(training);
    }

    @Override
    public Training update(Training entity) {
        return entityManager.merge(entity);
    }


    @Override
    public List<Training> findByTraineeUsername(String username, TrainingFilter filter) {
        return findByUsername(UserRole.TRAINEE, username, filter);
    }

    @Override
    public List<Training> findByTrainerUsername(String username, TrainingFilter filter) {
        return findByUsername(UserRole.TRAINER, username, filter);
    }

    private List<Training> findByUsername(
            UserRole role,
            String username,
            TrainingFilter filter) {

        StringBuilder jpql = new StringBuilder("SELECT t FROM Training t WHERE ");

        if (role == UserRole.TRAINEE) {
            jpql.append("t.trainee.username = :username");
        } else {
            jpql.append("t.trainer.username = :username");
        }

        Map<String, Object> params = new HashMap<>();
        params.put("username", username);

        if (filter != null) {

            if (filter.getFromDate() != null) {
                jpql.append(" AND t.trainingDate >= :fromDate");
                params.put("fromDate", filter.getFromDate());
            }

            if (filter.getToDate() != null) {
                jpql.append(" AND t.trainingDate <= :toDate");
                params.put("toDate", filter.getToDate());
            }

            if (filter.getTraineeFirstName() != null) {
                jpql.append(" AND t.trainee.firstName = :traineeFirstName");
                params.put("traineeFirstName", filter.getTraineeFirstName());
            }

            if (filter.getTraineeLastName() != null) {
                jpql.append(" AND t.trainee.lastName = :traineeLastName");
                params.put("traineeLastName", filter.getTraineeLastName());
            }

            if (filter.getTrainerFirstName() != null) {
                jpql.append(" AND t.trainer.firstName = :trainerFirstName");
                params.put("trainerFirstName", filter.getTrainerFirstName());
            }

            if (filter.getTrainerLastName() != null) {
                jpql.append(" AND t.trainer.lastName = :trainerLastName");
                params.put("trainerLastName", filter.getTrainerLastName());
            }

            if (filter.getTrainingType() != null) {
                jpql.append(" AND t.trainingType = :trainingType");
                params.put("trainingType", filter.getTrainingType());
            }
        }

        TypedQuery<Training> query =
                entityManager.createQuery(jpql.toString(), Training.class);

        for (Map.Entry<String, Object> e : params.entrySet()) {
            query.setParameter(e.getKey(), e.getValue());
        }

        return query.getResultList();
    }

    private enum UserRole {
        TRAINEE,
        TRAINER
    }
}