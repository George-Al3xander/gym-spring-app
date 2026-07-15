package io.github.George_Al3xander.dao;

import io.github.George_Al3xander.model.Trainer;

import java.util.List;
import java.util.Optional;

public interface TrainerDao extends CrudDao<Trainer> {
    Optional<Trainer> findByUsername(String username);

    List<Trainer> findAllByTraineeUsername(String username, boolean assigned);
}
