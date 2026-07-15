package io.github.George_Al3xander.service.impl;

import io.github.George_Al3xander.dao.TraineeDao;
import io.github.George_Al3xander.dao.TrainerDao;
import io.github.George_Al3xander.dao.TrainingDao;
import io.github.George_Al3xander.exception.EntityInUseException;
import io.github.George_Al3xander.exception.EntityNotFoundException;
import io.github.George_Al3xander.model.Trainee;
import io.github.George_Al3xander.model.Trainer;
import io.github.George_Al3xander.service.TraineeService;
import io.github.George_Al3xander.service.UsernameGenerator;
import io.github.George_Al3xander.util.PasswordGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TraineeServiceImpl implements TraineeService {
    private final TraineeDao traineeDao;

    private final TrainerDao trainerDao;

    private final TrainingDao trainingDao;

    private final UsernameGenerator usernameGenerator;

    @Override
    public Trainee getTraineeById(Long id) {
        Optional<Trainee> optionalTrainee = traineeDao.findById(id);

        if (optionalTrainee.isEmpty()) {
            throw new EntityNotFoundException("Trainee", id);
        }

        return optionalTrainee.get();
    }

    @Override
    public Trainee getTraineeByUsername(String username) {
        Optional<Trainee> optionalTrainee = traineeDao.findByUsername(username);

        if (optionalTrainee.isEmpty()) {
            throw new EntityNotFoundException("Trainee", username);
        }

        return optionalTrainee.get();
    }

    @Override
    public List<Trainee> getAllTrainees() {
        return traineeDao.findAll();
    }

    @Override
    public List<Trainee> getTraineesByTrainerUsername(String username, boolean assigned) {
        Optional<Trainer> trainerOptional = trainerDao.findByUsername(username);

        if (trainerOptional.isEmpty()) {
            throw new EntityNotFoundException("Trainer", username);
        }

        return traineeDao.findAllByTrainerUsername(trainerOptional.get().getUsername(), assigned);
    }

    @Override
    public Trainee saveTrainee(Trainee entity) {
        entity.setUsername(usernameGenerator.generateUsername(entity));
        entity.setPassword(PasswordGenerator.generatePassword(10));

        return traineeDao.save(entity);
    }

    @Override
    public Trainee updateTrainee(Trainee entity) {
        return traineeDao.update(entity);
    }

    @Override
    public void deleteTrainee(Long id) {
        trainingDao.findAll()
                .stream()
                .filter(t -> Objects.equals(t.getTrainee().getId(), id))
                .findFirst()
                .ifPresent(t -> {
                    throw new EntityInUseException(
                            "Trainee",
                            id.toString(),
                            "Training",
                            t.getTrainingName()
                    );
                });

        traineeDao.delete(id);
    }
}