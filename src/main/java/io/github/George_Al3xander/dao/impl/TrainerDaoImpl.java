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

        List<Trainer> trainers = entityManager
                .createQuery(qString, Trainer.class)
                .setParameter("username", username)
                .getResultList();

        return trainers.stream().findFirst();
    }
}