package io.github.George_Al3xander.mapper;

import io.github.George_Al3xander.dto.trainee.TraineeProfileResponse;
import io.github.George_Al3xander.dto.trainee.TraineeRegistrationRequest;
import io.github.George_Al3xander.dto.trainee.TraineeSummaryResponse;
import io.github.George_Al3xander.dto.trainer.TrainerSummaryResponse;
import io.github.George_Al3xander.model.Trainee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TraineeMapper {
    TraineeRegistrationRequest toRegistrationRequest(Trainee trainee);

    Trainee toTrainee(TraineeRegistrationRequest request);

    @Mapping(target = "trainers", source = "trainers")
    TraineeProfileResponse toTraineeProfile(
            Trainee trainee,
            List<TrainerSummaryResponse> trainers);

    TraineeSummaryResponse toSummary(Trainee trainee);
}

