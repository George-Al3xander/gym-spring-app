package io.github.George_Al3xander.service;

import io.github.George_Al3xander.model.TrainerWorkload;
import io.github.common.dto.trainer.TrainerWorkloadRequest;

public interface TrainerWorkloadService {
    TrainerWorkload handleTraining(TrainerWorkloadRequest request);

    TrainerWorkload getWorkloadByTrainerUsername(String username);
}
