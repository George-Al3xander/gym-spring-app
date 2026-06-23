package io.github.George_Al3xander.dao;

import io.github.George_Al3xander.model.Training;
import io.github.George_Al3xander.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class TrainingDao implements CrudDao<Training> {

    @Autowired
    private Storage storage;

    @Override
    public Training save(Training entity) {
        return storage.getTrainingStorage().put(UUID.randomUUID().toString(), entity);
    }

    @Override
    public Optional<Training> findById(String id) {
        return Optional.ofNullable(storage.getTrainingStorage().get(id));
    }

    @Override
    public List<Training> findAll() {
        return storage.getTrainingStorage().values().stream().toList();
    }

    @Override
    public void delete(String id) {
        storage.getTrainingStorage().remove(id);
    }

    @Override
    public Training update(Training entity) {
        return save(entity);
    }
}
