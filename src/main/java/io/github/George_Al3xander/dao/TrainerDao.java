package io.github.George_Al3xander.dao;

import io.github.George_Al3xander.model.Trainer;
import io.github.George_Al3xander.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class TrainerDao implements CrudDao<Trainer> {

    @Autowired
    private Storage storage;

    @Override
    public Trainer save(Trainer entity) {
        String id = UUID.randomUUID().toString();
        entity.setUserId(id);

        storage.getTrainerStorage().put(id, entity);
        return entity;
    }

    @Override
    public Optional<Trainer> findById(String id) {
        return Optional.ofNullable(storage.getTrainerStorage().get(id));
    }

    @Override
    public List<Trainer> findAll() {
        return List.copyOf(storage.getTrainerStorage().values());
    }

    @Override
    public void delete(String id) {
        storage.getTrainerStorage().remove(id);
    }

    @Override
    public Trainer update(Trainer entity) {
        if (!storage.getTrainerStorage().containsKey(entity.getUserId())) {
            throw new IllegalArgumentException(
                    "Trainer with id " + entity.getUserId() + " not found"
            );
        }

        storage.getTrainerStorage().put(entity.getUserId(), entity);
        return entity;
    }
}