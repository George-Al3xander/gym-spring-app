package io.github.George_Al3xander.dao.impl;

import io.github.George_Al3xander.dao.TrainerDao;
import io.github.George_Al3xander.model.Trainer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
    public List<Trainer> findUnassignedByTraineeUsername(String username) {
        String qString = "SELECT t FROM Trainer t WHERE EXISTS (SELECT u FROM User u WHERE u.username = :username) AND NOT EXISTS (SELECT tr.id FROM Training tr WHERE tr.trainer = t AND tr.trainee.username = :username)";

        return entityManager
                .createQuery(qString, Trainer.class)
                .setParameter("username", username)
                .getResultList();

    }
}