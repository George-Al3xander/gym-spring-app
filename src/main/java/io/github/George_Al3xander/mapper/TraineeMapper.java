package io.github.George_Al3xander.mapper;

import io.github.George_Al3xander.dto.trainee.TraineeRegistrationRequest;
import io.github.George_Al3xander.model.Trainee;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TraineeMapper {
    TraineeRegistrationRequest toRegistrationRequest(Trainee trainee);

    Trainee toTrainee(TraineeRegistrationRequest request);
}

