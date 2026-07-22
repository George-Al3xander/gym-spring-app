package io.github.George_Al3xander.dto.trainee;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTraineeTrainerListRequest {
    @NotEmpty
    List<String> trainers;
}
