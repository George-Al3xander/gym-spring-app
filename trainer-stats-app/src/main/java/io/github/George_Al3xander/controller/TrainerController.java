package io.github.George_Al3xander.controller;

import io.github.George_Al3xander.dto.workload.WorkloadRequest;
import io.github.George_Al3xander.model.TrainerWorkload;
import io.github.George_Al3xander.service.TrainerWorkloadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trainer")
@RequiredArgsConstructor
@Tag(
        name = "Trainer Workload",
        description = "Operations for managing and retrieving trainer workload"
)
public class TrainerController {

    private final TrainerWorkloadService trainerWorkloadService;

    @Operation(
            summary = "Update trainer workload",
            description = """
                    Adds or removes a training from the trainer's monthly workload.
                    
                    The action is determined by the actionType field:
                    - ADD: adds the training duration to the trainer's workload
                    - DELETE: removes the training duration from the trainer's workload
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Trainer workload successfully updated"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid workload request",
                    content = @Content(
                            mediaType = "application/json"
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - JWT token is missing or invalid"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    @PostMapping("/workload")
    public ResponseEntity<Void> addTraining(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Trainer workload event",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = WorkloadRequest.class)
                    )
            )
            @Valid @RequestBody WorkloadRequest request
    ) {
        trainerWorkloadService.handleTraining(request);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Get trainer workload",
            description = "Returns the complete workload summary for the specified trainer."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Trainer workload successfully retrieved",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TrainerWorkload.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Trainer workload not found"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - JWT token is missing or invalid"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    @GetMapping("/workload/{username}")
    public ResponseEntity<TrainerWorkload> getWorkload(
            @Parameter(
                    description = "Unique trainer username",
                    example = "john.smith",
                    required = true
            )
            @PathVariable(name = "username") String username
    ) {
        TrainerWorkload workload =
                trainerWorkloadService.getWorkloadByTrainerUsername(username);

        return ResponseEntity.ok(workload);
    }
}
