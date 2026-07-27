package io.github.George_Al3xander.dao.impl;

import io.github.George_Al3xander.dao.TrainingDao;
import io.github.George_Al3xander.dto.filter.TrainingFilter;
import io.github.George_Al3xander.model.Training;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.Root;
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

    @Override
    public int deleteForTraineeByTrainerUsernames(
            String traineeUsername,
            List<String> trainerUsernames
    ) {
        Long traineeId = entityManager.createQuery(
                        "select t.id from Trainee t where t.username = :username",
                        Long.class
                )
                .setParameter("username", traineeUsername)
                .getSingleResult();

        List<Long> trainerIds = entityManager.createQuery(
                        "select t.id from Trainer t where t.username in :usernames",
                        Long.class
                )
                .setParameter("usernames", trainerUsernames)
                .getResultList();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaDelete<Training> delete = cb.createCriteriaDelete(Training.class);

        Root<Training> training = delete.from(Training.class);

        delete.where(
                cb.and(
                        cb.equal(
                                training.get("trainee").get("id"),
                                traineeId
                        ),
                        cb.not(
                                training.get("trainer").get("id").in(trainerIds)
                        )
                )
        );

        return entityManager
                .createQuery(delete)
                .executeUpdate();
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

            if (filter.getTrainingTypeId() != null) {
                jpql.append(" AND t.trainingType.id = :trainingTypeId");
                params.put("trainingTypeId", filter.getTrainingTypeId());
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