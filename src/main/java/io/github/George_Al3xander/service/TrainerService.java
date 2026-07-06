package io.github.George_Al3xander.service;

import io.github.George_Al3xander.model.Trainer;

import java.util.List;

public interface TrainerService {

    Trainer getTrainerById(Long id);

    Trainer getTrainerByUsername(String username);

    List<Trainer> getAllTrainers();

    List<Trainer> getUnassignedTrainersByTraineeUsername(String username);

    Trainer saveTrainer(Trainer entity);

    Trainer updateTrainer(Trainer entity);
}
