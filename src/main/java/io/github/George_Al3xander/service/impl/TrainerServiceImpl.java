package io.github.George_Al3xander.service.impl;

import io.github.George_Al3xander.dao.TraineeDao;
import io.github.George_Al3xander.dao.TrainerDao;
import io.github.George_Al3xander.exception.EntityNotFoundException;
import io.github.George_Al3xander.model.Trainee;
import io.github.George_Al3xander.model.Trainer;
import io.github.George_Al3xander.service.TrainerService;
import io.github.George_Al3xander.service.UsernameGenerator;
import io.github.George_Al3xander.util.PasswordGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TrainerServiceImpl implements TrainerService {
    private final TrainerDao trainerDao;
    private final TraineeDao traineeDao;

    private final UsernameGenerator usernameGenerator;

    @Override
    public Trainer getTrainerById(Long id) {
        Optional<Trainer> optionalTrainer = trainerDao.findById(id);

        if (optionalTrainer.isEmpty()) {
            throw new EntityNotFoundException("Trainer", id);
        }

        return optionalTrainer.get();
    }

    @Override
    public Trainer getTrainerByUsername(String username) {
        Optional<Trainer> optionalTrainer = trainerDao.findByUsername(username);

        if (optionalTrainer.isEmpty()) {
            throw new EntityNotFoundException("Trainer", username);
        }

        return optionalTrainer.get();
    }

    @Override
    public List<Trainer> getAllTrainers() {
        return trainerDao.findAll();
    }

    @Override
    public List<Trainer> getTrainersByTraineeUsername(String username, boolean assigned) {
        Optional<Trainee> traineeOptional = traineeDao.findByUsername(username);

        if (traineeOptional.isEmpty()) {
            throw new EntityNotFoundException("Trainee", username);
        }

        return trainerDao.findAllByTraineeUsername(traineeOptional.get().getUsername(), assigned);
    }

    @Override
    public Trainer saveTrainer(Trainer entity) {
        entity.setUsername(usernameGenerator.generateUsername(entity));
        entity.setPassword(PasswordGenerator.generatePassword(10));

        return trainerDao.save(entity);
    }

    @Override
    public Trainer updateTrainer(Trainer entity) {
        return trainerDao.update(entity);
    }
}