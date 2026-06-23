package io.github.George_Al3xander.dao;

import io.github.George_Al3xander.model.Trainee;
import io.github.George_Al3xander.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class TraineeDao implements CrudDao<Trainee> {

    @Autowired
    private Storage storage;

    @Override
    public Trainee save(Trainee entity) {
        return storage.getTraineeStorage().put(UUID.randomUUID().toString(), entity);

    }

    @Override
    public Optional<Trainee> findById(String id) {
        return Optional.ofNullable(storage.getTraineeStorage().get(id));
    }

    @Override
    public List<Trainee> findAll() {
        return storage.getTraineeStorage().values().stream().toList();
    }

    @Override
    public void delete(String id) {
        storage.getTraineeStorage().remove(id);
    }

    @Override
    public Trainee update(Trainee entity) {
        return storage.getTraineeStorage().put(entity.getUserId(), entity);
    }
}
