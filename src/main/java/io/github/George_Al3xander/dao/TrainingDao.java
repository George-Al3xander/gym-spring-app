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
        String id = UUID.randomUUID().toString();

        storage.getTrainingStorage().put(id, entity);

        return entity;
    }

    @Override
    public Optional<Training> findById(String id) {
        return Optional.ofNullable(storage.getTrainingStorage().get(id));
    }

    @Override
    public List<Training> findAll() {
        return List.copyOf(storage.getTrainingStorage().values());
    }

    @Override
    public void delete(String id) {
        storage.getTrainingStorage().remove(id);
    }

    @Override
    public Training update(Training entity) {
        String existingId = storage.getTrainingStorage()
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue().equals(entity))
                .map(entry -> entry.getKey())
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("Training not found")
                );

        storage.getTrainingStorage().put(existingId, entity);

        return entity;
    }
}