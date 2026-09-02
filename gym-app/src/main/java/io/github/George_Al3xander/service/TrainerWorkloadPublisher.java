package io.github.George_Al3xander.service;

import io.github.George_Al3xander.model.Training;

public interface TrainerWorkloadPublisher {
    void publish(Training training);
}
