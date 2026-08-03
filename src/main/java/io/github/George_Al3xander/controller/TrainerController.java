package io.github.George_Al3xander.controller;

import io.github.George_Al3xander.dto.auth.CredentialsDTO;
import io.github.George_Al3xander.dto.filter.TrainingFilter;
import io.github.George_Al3xander.dto.trainer.TrainerProfileResponse;
import io.github.George_Al3xander.dto.trainer.TrainerRegistrationRequest;
import io.github.George_Al3xander.dto.trainer.TrainerTrainingResponse;
import io.github.George_Al3xander.dto.trainer.UpdateTrainerRequest;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trainers")
@RequiredArgsConstructor
@Tag(
        name = "Trainer Management",
        description = "Operations related to trainer profiles and trainings"
)
public class TrainerController {

    private final GymFacade gymFacade;

    @PostMapping
    @Operation(
            summary = "Create trainer",
            description = "Registers a new trainer and returns generated credentials"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Trainer successfully created",
                    content = @Content(schema = @Schema(implementation = CredentialsDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error"
            )
    })
    public CredentialsDTO createTrainer(
            @Parameter(description = "Trainer registration data", required = true)
            @Valid @RequestBody TrainerRegistrationRequest trainerRegistrationRequest
    ) {
        return gymFacade.createTrainer(trainerRegistrationRequest);
    }

    @GetMapping("/{username}")
    @Operation(
            summary = "Get trainer profile",
            description = "Returns trainer profile information by username"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Trainer profile found",
                    content = @Content(schema = @Schema(implementation = TrainerProfileResponse.class))
            ),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Trainer not found")
    })
    public TrainerProfileResponse getTrainerByUsername(
            @Parameter(description = "Trainer username")
            @PathVariable String username
    ) {
        return gymFacade.getTrainer(username);
    }

    @PutMapping("/{username}")
    @Operation(
            summary = "Update trainer profile",
            description = "Updates trainer personal information"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Trainer updated successfully",
                    content = @Content(schema = @Schema(implementation = TrainerProfileResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<TrainerProfileResponse> updateTrainerByUsername(
            @Parameter(description = "Trainer username")
            @PathVariable String username,
            @Parameter(description = "Trainer update data", required = true)
            @Valid @RequestBody UpdateTrainerRequest request
    ) {

        return ResponseEntity.ok(
                gymFacade.updateTrainer(username, request)
        );
    }

    @GetMapping("/{username}/trainings")
    @Operation(
            summary = "Get trainer trainings",
            description = "Returns trainer training history filtered by criteria"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Training list returned",
                    content = @Content(
                            array = @ArraySchema(
                                    schema = @Schema(implementation = TrainerTrainingResponse.class)
                            )
                    )
            ),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<List<TrainerTrainingResponse>> getTrainings(
            @Parameter(description = "Trainer username")
            @PathVariable String username,
            @Parameter(description = "Training filter criteria", required = true)
            @Valid @RequestBody TrainingFilter trainingFilter
    ) {

        return ResponseEntity.ok(
                gymFacade.getTrainerTrainings(
                        username,
                        trainingFilter
                )
        );
    }

    @PatchMapping("/{username}/activate")
    @Operation(
            summary = "Activate or deactivate trainer",
            description = "Changes trainer active status"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Status updated successfully"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden"
            )
    })
    public ResponseEntity<Void> activateTrainer(
            @Parameter(description = "Trainer username")
            @PathVariable String username,
            @Parameter(description = "Activation status request", required = true)
            @Valid @RequestBody ActivateUserRequest request
    ) {

        gymFacade.updateActiveStatusByUsername(
                username,
                request.isActive()
        );

        return ResponseEntity.noContent().build();
    }
}