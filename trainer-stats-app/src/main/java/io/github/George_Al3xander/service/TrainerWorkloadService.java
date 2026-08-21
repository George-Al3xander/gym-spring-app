package io.github.George_Al3xander.service;

import io.github.George_Al3xander.dto.workload.WorkloadRequest;
import io.github.George_Al3xander.model.TrainerWorkload;

public interface TrainerWorkloadService {
    TrainerWorkload handleTraining(WorkloadRequest request);
}
