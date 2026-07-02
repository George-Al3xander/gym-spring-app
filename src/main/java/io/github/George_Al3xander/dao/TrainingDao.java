package io.github.George_Al3xander.dao;

import io.github.George_Al3xander.model.Training;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TrainingDao implements CrudDao<Training> {

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
}