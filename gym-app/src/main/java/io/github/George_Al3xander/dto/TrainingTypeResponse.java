package io.github.George_Al3xander.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainingTypeResponse {
    private Long id;

    private String trainingTypeName;
}
