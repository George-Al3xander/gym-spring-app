package io.github.George_Al3xander.controller;

import io.github.George_Al3xander.dto.auth.CredentialsDTO;
import io.github.George_Al3xander.dto.filter.TrainerFilter;
import io.github.George_Al3xander.dto.filter.TrainingFilter;
import io.github.George_Al3xander.dto.trainee.*;
import io.github.George_Al3xander.dto.trainer.TrainerSummaryResponse;
import io.github.George_Al3xander.facade.GymFacade;
import io.github.George_Al3xander.model.Trainee;
import io.github.George_Al3xander.web.AuthHttpHeader;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PutMapping("/{username}")
    public ResponseEntity<TraineeProfileResponse> updateTraineeByUsername(
            @PathVariable("username") String username,
            @RequestHeader(AuthHttpHeader.USERNAME) String authUsername,
            @Valid @RequestBody UpdateTraineeRequest request
    ) {
        if (!username.equals(authUsername)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(gymFacade.updateTrainee(username, request));
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteTraineeByUsername(
            @PathVariable("username") String username,
            @RequestHeader(AuthHttpHeader.USERNAME) String authUsername
    ) {
        if (!username.equals(authUsername)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        gymFacade.deleteTrainee(username);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{username}/unassigned-trainers")
    public ResponseEntity<List<TrainerSummaryResponse>> getUnassignedTrainers(
            @PathVariable("username") String username,
            @RequestHeader(AuthHttpHeader.USERNAME) String authUsername
    ) {
        if (!username.equals(authUsername)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(
                gymFacade.getTrainersByTraineeUsername(
                        username,
                        new TrainerFilter(true, false)
                )
        );
    }

    @GetMapping("/{username}/trainings")
    public ResponseEntity<List<TraineeTrainingResponse>> getTrainings(
            @PathVariable("username") String username,
            @RequestHeader(AuthHttpHeader.USERNAME) String authUsername,
            @Valid @RequestBody TrainingFilter trainingFilter
    ) {
        if (!username.equals(authUsername)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(
                gymFacade.getTraineeTrainings(
                        username,
                        trainingFilter
                )
        );
    }

    @PutMapping("/{username}/trainers")
    public ResponseEntity<List<TrainerSummaryResponse>> updateTraineeTrainersList(
            @PathVariable("username") String username,
            @RequestHeader(AuthHttpHeader.USERNAME) String authUsername,
            @Valid @RequestBody UpdateTraineeTrainerListRequest request
    ) {
        if (!username.equals(authUsername)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(
                gymFacade.updateTrainersListByTraineeUsername(username, request)
        );
    }
}
