package io.github.George_Al3xander.dao;

import io.github.George_Al3xander.exception.EntityNotFoundException;
import io.github.George_Al3xander.model.Trainer;
import io.github.George_Al3xander.storage.Storage;
import io.github.George_Al3xander.util.SequenceGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TrainerDao implements CrudDao<Trainer> {

    @Autowired
    private Storage storage;

    private SequenceGenerator sequenceGenerator;

    @Override
    public Trainer save(Trainer entity) {
        long id = sequenceGenerator.getNextSeq();
        entity.setUserId(id);

        storage.getTrainerStorage().put(id, entity);
        return entity;
    }

    @Override
    public Optional<Trainer> findById(Long id) {
        return Optional.ofNullable(storage.getTrainerStorage().get(id));
    }

    @Override
    public List<Trainer> findAll() {
        return List.copyOf(storage.getTrainerStorage().values());
    }

    @Override
    public void delete(Long id) {
        storage.getTrainerStorage().remove(id);
    }

    @Override
    public Trainer update(Trainer entity) {
        if (!storage.getTrainerStorage().containsKey(entity.getUserId())) {
            throw new EntityNotFoundException(
                    "Trainer", entity.getUserId()
            );
        }

        storage.getTrainerStorage().put(entity.getUserId(), entity);
        return entity;
    }

    @Autowired
    public void setSequenceGenerator(SequenceGenerator sequenceGenerator) {
        this.sequenceGenerator = sequenceGenerator;
    }
}