package io.github.George_Al3xander.dto.trainee;

import io.github.George_Al3xander.dto.trainer.TrainerSummaryResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TraineeProfileResponse {
    private String username;
    
    private String firstName;

    private String lastName;

    private LocalDate dateOfBirth;

    private String address;

    private Boolean isActive;

    private List<TrainerSummaryResponse> trainers = new ArrayList<>();
}
