package io.github.George_Al3xander.dto.trainer;


import io.github.George_Al3xander.dto.TrainingTypeResponse;
import io.github.George_Al3xander.dto.trainee.TraineeSummaryResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainerProfileResponse {
    private String firstName;

    private String lastName;

    private TrainingTypeResponse trainingTypeResponse;

    private Boolean isActive;

    private List<TraineeSummaryResponse> trainees = new ArrayList<>();
}
