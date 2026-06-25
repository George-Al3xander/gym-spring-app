package io.github.George_Al3xander.dao;

import io.github.George_Al3xander.exception.EntityNotFoundException;
import io.github.George_Al3xander.model.Trainee;
import io.github.George_Al3xander.storage.Storage;
import io.github.George_Al3xander.util.SequenceGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TraineeDao implements CrudDao<Trainee> {

    @Autowired
    private Storage storage;

    private SequenceGenerator sequenceGenerator;

    @Override
    public Trainee save(Trainee entity) {
        long id = sequenceGenerator.getNextSeq();
        entity.setUserId(id);

        storage.getTraineeStorage().put(id, entity);
        return entity;
    }

    @Override
    public Optional<Trainee> findById(Long id) {
        return Optional.ofNullable(storage.getTraineeStorage().get(id));
    }

    @Override
    public List<Trainee> findAll() {
        return List.copyOf(storage.getTraineeStorage().values());
    }

    @Override
    public void delete(Long id) {
        storage.getTraineeStorage().remove(id);
    }

    @Override
    public Trainee update(Trainee entity) {
        if (!storage.getTraineeStorage().containsKey(entity.getUserId())) {
            throw new EntityNotFoundException(
                    "Trainee", entity.getUserId()
            );
        }

        storage.getTraineeStorage().put(entity.getUserId(), entity);
        return entity;
    }

    @Autowired
    public void setSequenceGenerator(SequenceGenerator sequenceGenerator) {
        this.sequenceGenerator = sequenceGenerator;
    }
}