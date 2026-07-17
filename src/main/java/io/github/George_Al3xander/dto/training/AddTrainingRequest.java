package io.github.George_Al3xander.dto.training;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddTrainingRequest {
    @NotBlank
    private String traineeUsername;

    @NotBlank
    private String trainerUsername;

    @NotBlank
    private String trainingName;

    @NotNull
    private LocalDateTime trainingDate;

    @Positive
    private int durationSeconds;
}
