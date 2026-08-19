package io.github.George_Al3xander.dao;

import io.github.George_Al3xander.model.Trainee;

import java.util.List;
import java.util.Optional;

public interface TraineeDao extends CrudDao<Trainee> {
    Optional<Trainee> findByUsername(String username);

    List<Trainee> findAllByTrainerUsername(String username, boolean assigned);
}
