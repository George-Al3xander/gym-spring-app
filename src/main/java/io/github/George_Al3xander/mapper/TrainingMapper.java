package io.github.George_Al3xander.mapper;

import io.github.George_Al3xander.dto.trainee.TraineeTrainingResponse;
import io.github.George_Al3xander.dto.trainer.TrainerTrainingResponse;
import io.github.George_Al3xander.model.Training;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TrainingMapper {

    TraineeTrainingResponse toTraineeResponse(Training training);

    TrainerTrainingResponse toTrainerResponse(Training training);
}
