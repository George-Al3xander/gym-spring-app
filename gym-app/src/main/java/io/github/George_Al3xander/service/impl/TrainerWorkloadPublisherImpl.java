package io.github.George_Al3xander.service.impl;

import io.github.George_Al3xander.model.Trainer;
import io.github.George_Al3xander.model.Training;
import io.github.George_Al3xander.service.TrainerWorkloadPublisher;
import io.github.common.dto.trainer.TrainerWorkloadRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrainerWorkloadPublisherImpl implements TrainerWorkloadPublisher {
    private final JmsClient jmsClient;

    @Value("${app.trainer-workload.queue-name}")
    private String trainerWorkloadQueueName;

    @Value("${correlation-id.header}")
    private String correlationIdKey;

    @Override
    public void publish(Training training) {
        Trainer trainer = training.getTrainer();

        TrainerWorkloadRequest trainerWorkloadRequest = TrainerWorkloadRequest.builder()
                .correlationId(MDC.get(correlationIdKey))
                .trainerUsername(trainer.getUsername())
                .trainerFirstName(trainer.getFirstName())
                .trainerLastName(trainer.getLastName())
                .active(trainer.getIsActive())
                .actionType(TrainerWorkloadRequest.ActionType.ADD)
                .trainingDate(training.getTrainingDate())
                .trainingDuration(training.getDurationSeconds())
                .build();

        jmsClient
                .destination(trainerWorkloadQueueName)
                .send(trainerWorkloadRequest);
    }
}
