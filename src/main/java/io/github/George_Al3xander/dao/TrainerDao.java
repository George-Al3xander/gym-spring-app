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
        return storage.getTrainerStorage().put(UUID.randomUUID().toString(), entity);

    }

    @Override
    public Optional<Trainer> findById(String id) {
        return Optional.ofNullable(storage.getTrainerStorage().get(id));
    }

    @Override
    public List<Trainer> findAll() {
        return storage.getTrainerStorage().values().stream().toList();
    }

    @Override
    public void delete(String id) {
        storage.getTrainerStorage().remove(id);
    }

    @Override
    public Trainer update(Trainer entity) {
        return storage.getTrainerStorage().put(entity.getUserId(), entity);
    }
}
