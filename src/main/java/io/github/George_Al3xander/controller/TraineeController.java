package io.github.George_Al3xander.controller;

import io.github.George_Al3xander.dto.auth.CredentialsDTO;
import io.github.George_Al3xander.dto.filter.TrainerFilter;
import io.github.George_Al3xander.dto.filter.TrainingFilter;
import io.github.George_Al3xander.dto.trainee.*;
import io.github.George_Al3xander.dto.trainer.TrainerSummaryResponse;
import io.github.George_Al3xander.dto.user.ActivateUserRequest;
import io.github.George_Al3xander.facade.GymFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trainees")
@RequiredArgsConstructor
@Tag(
        name = "Trainee Management",
        description = "Operations related to trainee profiles and trainings"
)
public class TraineeController {

    private final GymFacade gymFacade;

    @PostMapping
    @Operation(
            summary = "Create trainee",
            description = "Registers a new trainee and returns generated credentials"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Trainee successfully created",
                    content = @Content(schema = @Schema(implementation = CredentialsDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error"
            )
    })
    public CredentialsDTO createTrainee(
            @Parameter(description = "Trainee registration data", required = true)
            @Valid @RequestBody TraineeRegistrationRequest traineeRegistrationRequest
    ) {
        return gymFacade.createTrainee(traineeRegistrationRequest);
    }

    @GetMapping("/{username}")
    @Operation(
            summary = "Get trainee profile",
            description = "Returns trainee profile information by username"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Profile found",
                    content = @Content(schema = @Schema(implementation = TraineeProfileResponse.class))
            ),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Trainee not found")
    })
    public TraineeProfileResponse getTraineeByUsername(
            @Parameter(description = "Trainee username")
            @PathVariable String username
    ) {
        return gymFacade.getTrainee(username);
    }

    @PutMapping("/{username}")
    @Operation(
            summary = "Update trainee profile",
            description = "Updates trainee personal information"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Trainee updated",
                    content = @Content(schema = @Schema(implementation = TraineeProfileResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<TraineeProfileResponse> updateTraineeByUsername(
            @Parameter(description = "Trainee username")
            @PathVariable String username,
            @Valid @RequestBody UpdateTraineeRequest request
    ) {
        return ResponseEntity.ok(
                gymFacade.updateTrainee(username, request)
        );
    }

    @DeleteMapping("/{username}")
    @Operation(
            summary = "Delete trainee",
            description = "Deletes trainee profile"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Trainee deleted"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<Void> deleteTraineeByUsername(
            @Parameter(description = "Trainee username")
            @PathVariable String username
    ) {

        gymFacade.deleteTrainee(username);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{username}/unassigned-trainers")
    @Operation(
            summary = "Get unassigned trainers",
            description = "Returns active trainers not assigned to trainee"
    )
    @ApiResponse(
            responseCode = "200",
            description = "List of available trainers",
            content = @Content(
                    array = @ArraySchema(schema = @Schema(implementation = TrainerSummaryResponse.class))
            )
    )
    public ResponseEntity<List<TrainerSummaryResponse>> getUnassignedTrainers(
            @Parameter(description = "Trainee username")
            @PathVariable String username
    ) {

        return ResponseEntity.ok(
                gymFacade.getTrainersByTraineeUsername(
                        username,
                        new TrainerFilter(true, false)
                )
        );
    }

    @GetMapping("/{username}/trainings")
    @Operation(
            summary = "Get trainee trainings",
            description = "Returns trainee training history filtered by criteria"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Training list",
            content = @Content(
                    array = @ArraySchema(schema = @Schema(implementation = TraineeTrainingResponse.class))
            )
    )
    public ResponseEntity<List<TraineeTrainingResponse>> getTrainings(

            @Parameter(description = "Trainee username")
            @PathVariable String username,
            @ParameterObject @ModelAttribute TrainingFilter trainingFilter
    ) {

        return ResponseEntity.ok(
                gymFacade.getTraineeTrainings(username, trainingFilter)
        );
    }

    @PutMapping("/{username}/trainers")
    @Operation(
            summary = "Update trainee trainers list",
            description = "Assigns trainers to trainee"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Updated trainers list",
            content = @Content(
                    array = @ArraySchema(schema = @Schema(implementation = TrainerSummaryResponse.class))
            )
    )
    public ResponseEntity<List<TrainerSummaryResponse>> updateTraineeTrainersList(
            @Parameter(description = "Trainee username")
            @PathVariable String username,
            @Valid @RequestBody UpdateTraineeTrainerListRequest request
    ) {

        return ResponseEntity.ok(
                gymFacade.updateTrainersListByTraineeUsername(
                        username,
                        request
                )
        );
    }

    @PatchMapping("/{username}/activate")
    @Operation(
            summary = "Activate or deactivate trainee",
            description = "Changes trainee active status"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Status updated"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<Void> activateTrainee(
            @Parameter(description = "Trainee username")
            @PathVariable String username,
            @Valid @RequestBody ActivateUserRequest request
    ) {

        gymFacade.updateActiveStatusByUsername(
                username,
                request.isActive()
        );

        return ResponseEntity.noContent().build();
    }
}