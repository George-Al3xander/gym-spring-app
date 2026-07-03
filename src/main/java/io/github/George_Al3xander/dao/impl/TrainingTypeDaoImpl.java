package io.github.George_Al3xander.dao.impl;

import io.github.George_Al3xander.dao.TrainingTypeDao;
import io.github.George_Al3xander.model.TrainingType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TrainingTypeDaoImpl implements TrainingTypeDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public TrainingType save(TrainingType entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public Optional<TrainingType> findById(Long id) {
        return Optional.ofNullable(entityManager.find(TrainingType.class, id));
    }

    @Override
    public List<TrainingType> findAll() {
        return entityManager
                .createQuery("SELECT tt FROM TrainingType tt", TrainingType.class)
                .getResultList();
    }

    @Override
    public void delete(Long id) {
        TrainingType trainingType = entityManager.getReference(TrainingType.class, id);
        entityManager.remove(trainingType);
    }

    @Override
    public TrainingType update(TrainingType entity) {
        return entityManager.merge(entity);
    }
}
