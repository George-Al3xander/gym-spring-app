package io.github.George_Al3xander.dao.impl;

import io.github.George_Al3xander.dao.TrainerDao;
import io.github.George_Al3xander.model.Trainer;
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
public class TrainerDaoImpl implements TrainerDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Trainer save(Trainer entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public Optional<Trainer> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Trainer.class, id));
    }

    @Override
    public List<Trainer> findAll() {
        return entityManager
                .createQuery("SELECT t FROM Trainer t", Trainer.class)
                .getResultList();
    }

    @Override
    public void delete(Long id) {
        Trainer trainer = entityManager.getReference(Trainer.class, id);
        entityManager.remove(trainer);
    }

    @Override
    public Trainer update(Trainer entity) {
        return entityManager.merge(entity);
    }

    @Override
    public Optional<Trainer> findByUsername(String username) {
        String qString = "SELECT t FROM Trainer t WHERE t.username = :username";

        Trainer trainer = entityManager
                .createQuery(qString, Trainer.class)
                .setParameter("username", username)
                .getSingleResult();

        return Optional.of(trainer);
    }

    @Override
    public List<Trainer> findByTraineeUsername(String username, boolean assigned) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Trainer> cq = cb.createQuery(Trainer.class);

        Root<Trainer> trainer = cq.from(Trainer.class);

        Subquery<Long> sq = cq.subquery(Long.class);
        Root<Training> training = sq.from(Training.class);

        sq.select(cb.literal(1L))
                .where(
                        cb.equal(training.get("trainer"), trainer),
                        cb.equal(training.get("trainee").get("username"), username)
                );

        if (assigned) {
            cq.where(cb.exists(sq));
        } else {
            cq.where(cb.not(cb.exists(sq)));
        }

        return entityManager.createQuery(cq).getResultList();

    }
}