package io.github.George_Al3xander.dao;

import io.github.George_Al3xander.model.Trainee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TraineeDao implements CrudDao<Trainee> {

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
}