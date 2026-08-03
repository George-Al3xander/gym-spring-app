package io.github.George_Al3xander.controller;

import io.github.George_Al3xander.dto.training.AddTrainingRequest;
import io.github.George_Al3xander.facade.GymFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/trainings")
@RequiredArgsConstructor
@Tag(
        name = "Training Management",
        description = "Operations related to adding trainings"
)
public class TrainingController {

    private final GymFacade gymFacade;

    @PostMapping
    @Operation(
            summary = "Add training",
            description = "Creates a new training session between trainee and trainer"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Training successfully created"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentication failed"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Trainee, trainer, or training type not found"
            )
    })
    public ResponseEntity<Void> addTraining(
            @Parameter(
                    description = "Training creation data",
                    required = true
            )
            @Valid @RequestBody AddTrainingRequest request
    ) {
        gymFacade.addTraining(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}