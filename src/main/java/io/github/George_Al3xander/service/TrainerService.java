package io.github.George_Al3xander.service;

import io.github.George_Al3xander.model.Trainer;

import java.util.List;

public interface TrainerService {

    Trainer getTrainerById(String id);

    List<Trainer> getAllTrainers();

    Trainer saveTrainer(Trainer entity);

    Trainer updateTrainer(Trainer entity);
}
