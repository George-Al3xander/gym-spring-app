package io.github.George_Al3xander.controller;

import io.github.George_Al3xander.dto.CredentialsDTO;
import io.github.George_Al3xander.dto.trainee.TraineeProfileResponse;
import io.github.George_Al3xander.dto.trainee.TraineeRegistrationRequest;
import io.github.George_Al3xander.facade.GymFacade;
import io.github.George_Al3xander.model.Trainee;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/trainees")
@RequiredArgsConstructor
public class TraineeController {

    private final GymFacade gymFacade;

    @PostMapping
    public CredentialsDTO createTrainee(
            @Valid @RequestBody TraineeRegistrationRequest traineeRegistrationRequest
    ) {
        Trainee trainee = gymFacade.createTrainee(traineeRegistrationRequest);

        return new CredentialsDTO(trainee.getUsername(), trainee.getPassword());
    }

    @GetMapping("/{username}")
    public TraineeProfileResponse getTraineeByUsername(@PathVariable("username") String username) {
        return gymFacade.getTrainee(username);
    }
}
