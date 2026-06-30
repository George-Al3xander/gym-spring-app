package io.github.George_Al3xander.storage;

import io.github.George_Al3xander.model.Trainee;
import io.github.George_Al3xander.model.Trainer;
import io.github.George_Al3xander.model.Training;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Getter
@RequiredArgsConstructor
public class InMemoryStorage implements Storage {

    private final Map<Long, Trainee> traineeStorage;

    private final Map<Long, Trainer> trainerStorage;

    private final Map<Long, Training> trainingStorage;

}