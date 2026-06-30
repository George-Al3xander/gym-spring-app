package io.github.George_Al3xander.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Training {

    private Long traineeId;

    private Long trainerId;

    private String trainingName;

    private TrainingType trainingType;

    private LocalDateTime trainingDate;

    private Integer durationSeconds;
}