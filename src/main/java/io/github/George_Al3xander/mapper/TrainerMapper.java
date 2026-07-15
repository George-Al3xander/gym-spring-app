package io.github.George_Al3xander.mapper;

import io.github.George_Al3xander.dto.trainer.TrainerRegistrationRequest;
import io.github.George_Al3xander.dto.trainer.TrainerSummaryResponse;
import io.github.George_Al3xander.model.Trainer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TrainerMapper {
    TrainerSummaryResponse toSummary(Trainer trainer);

    Trainer toTrainer(TrainerRegistrationRequest request);
}
