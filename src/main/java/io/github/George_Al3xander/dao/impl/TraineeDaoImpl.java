package io.github.George_Al3xander.dao.impl;

import io.github.George_Al3xander.dao.TraineeDao;
import io.github.George_Al3xander.model.Trainee;
import io.github.George_Al3xander.model.Training;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TraineeDaoImpl implements TraineeDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Trainee save(Trainee entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public Optional<Trainee> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Trainee.class, id));
    }

    @Override
    public List<Trainee> findAll() {
        return entityManager
                .createQuery("SELECT t FROM Trainee t", Trainee.class)
                .getResultList();
    }

    @Override
    public void delete(Long id) {
        Trainee trainee = entityManager.getReference(Trainee.class, id);
        entityManager.remove(trainee);
    }

    @Override
    public Trainee update(Trainee entity) {
        return entityManager.merge(entity);
    }

    @Override
    public Optional<Trainee> findByUsername(String username) {
        String qString = "SELECT t FROM Trainee t WHERE t.username = :username";

        Trainee trainee = entityManager
                .createQuery(qString, Trainee.class)
                .setParameter("username", username)
                .getSingleResult();

        return Optional.ofNullable(trainee);
    }

    @Override
    public List<Trainee> findAllByTrainerUsername(String username, boolean assigned) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Trainee> cq = cb.createQuery(Trainee.class);

        Root<Trainee> trainee = cq.from(Trainee.class);

        Subquery<Long> sq = cq.subquery(Long.class);
        Root<Training> training = sq.from(Training.class);

        sq.select(cb.literal(1L))
                .where(
                        cb.equal(training.get("trainee"), trainee),
                        cb.equal(training.get("trainer").get("username"), username)
                );

        if (assigned) {
            cq.where(cb.exists(sq));
        } else {
            cq.where(cb.not(cb.exists(sq)));
        }

        return entityManager.createQuery(cq).getResultList();
    }
}