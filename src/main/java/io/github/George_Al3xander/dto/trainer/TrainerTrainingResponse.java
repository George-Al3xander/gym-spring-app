package io.github.George_Al3xander.dto.trainer;

import io.github.George_Al3xander.dto.TrainingTypeResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainerTrainingResponse {
    private String trainingName;

    private LocalDateTime trainingDate;

    private TrainingTypeResponse trainingType;

    private int durationSeconds;

    private String traineeName;
}
