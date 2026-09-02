package io.github.George_Al3xander.dto.trainer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainerWorkloadRequest implements Serializable {
    @NotBlank
    private String correlationId;

    @NotBlank
    private String trainerUsername;

    @NotBlank
    private String trainerFirstName;

    @NotBlank
    private String trainerLastName;

    @NotNull
    private Boolean active;

    @NotNull
    private LocalDateTime trainingDate;

    @Positive
    private int trainingDuration;

    @NotNull
    private ActionType actionType;

    public enum ActionType {
        ADD,
        DELETE
    }
}
