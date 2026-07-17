package io.github.George_Al3xander.dto.filter;

import io.github.George_Al3xander.validation.date.ChronologicalDates;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ChronologicalDates(from = "fromDate", to = "toDate")
public class TrainingFilter {
    private LocalDateTime fromDate;
    private LocalDateTime toDate;

    private String traineeFirstName;
    private String traineeLastName;

    private String trainerFirstName;
    private String trainerLastName;

    private Long trainingTypeId;
}
