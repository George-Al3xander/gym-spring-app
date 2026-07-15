package io.github.George_Al3xander.mapper;

import io.github.George_Al3xander.dto.trainee.TraineeSummaryResponse;
import io.github.George_Al3xander.dto.trainer.TrainerProfileResponse;
import io.github.George_Al3xander.dto.trainer.TrainerRegistrationRequest;
import io.github.George_Al3xander.dto.trainer.TrainerSummaryResponse;
import io.github.George_Al3xander.model.Trainer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TrainerMapper {
    TrainerSummaryResponse toSummary(Trainer trainer);

    Trainer toTrainer(TrainerRegistrationRequest request);

    @Mapping(target = "trainees", source = "trainees")
    TrainerProfileResponse toTrainerProfile(
            Trainer trainer,
            List<TraineeSummaryResponse> trainees
    );
}
