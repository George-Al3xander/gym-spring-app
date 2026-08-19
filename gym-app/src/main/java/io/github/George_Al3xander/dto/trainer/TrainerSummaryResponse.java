package io.github.George_Al3xander.dto.trainer;

import io.github.George_Al3xander.dto.TrainingTypeResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainerSummaryResponse {
    private String username;

    private String firstName;

    private String lastName;

    private TrainingTypeResponse specialization;
}
