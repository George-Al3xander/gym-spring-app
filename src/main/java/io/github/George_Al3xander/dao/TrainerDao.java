package io.github.George_Al3xander.dao;

import io.github.George_Al3xander.model.Trainer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TrainerDao implements CrudDao<Trainer> {

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

}