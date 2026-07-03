package io.github.George_Al3xander.dao;

import io.github.George_Al3xander.dto.TrainingFilter;
import io.github.George_Al3xander.model.Training;

import java.util.List;

public interface TrainingDao extends CrudDao<Training> {
    List<Training> findByTraineeUsername(String username, TrainingFilter filter);

    List<Training> findByTrainerUsername(String username, TrainingFilter filter);
}
