package io.github.George_Al3xander.repository;

import io.github.George_Al3xander.model.TrainerWorkload;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrainerWorkloadRepository extends MongoRepository<TrainerWorkload, Long> {
    Optional<TrainerWorkload> findByTrainerUsername(String trainerUsername);
}
