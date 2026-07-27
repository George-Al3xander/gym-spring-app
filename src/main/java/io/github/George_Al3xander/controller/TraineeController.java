package io.github.George_Al3xander.controller;

import io.github.George_Al3xander.dto.auth.CredentialsDTO;
import io.github.George_Al3xander.dto.filter.TrainerFilter;
import io.github.George_Al3xander.dto.filter.TrainingFilter;
import io.github.George_Al3xander.dto.trainee.*;
import io.github.George_Al3xander.dto.trainer.TrainerSummaryResponse;
import io.github.George_Al3xander.dto.user.ActivateUserRequest;
import io.github.George_Al3xander.facade.GymFacade;
import io.github.George_Al3xander.model.Trainee;
import io.github.George_Al3xander.web.AuthHttpHeader;
import io.swagger.annotations.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/trainees")
@RequiredArgsConstructor
@Api(tags = "Trainee Management", description = "Operations related to trainee profiles and trainings")
public class TraineeController {

    private final GymFacade gymFacade;


    @PostMapping
    @ApiOperation(
            value = "Create trainee",
            notes = "Registers a new trainee and returns generated credentials"
    )
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Trainee successfully created",
                    response = CredentialsDTO.class
            ),
            @ApiResponse(
                    code = 400,
                    message = "Validation error"
            )
    })
    public CredentialsDTO createTrainee(
            @ApiParam(
                    value = "Trainee registration data",
                    required = true
            )
            @Valid @RequestBody TraineeRegistrationRequest traineeRegistrationRequest
    ) {
        Trainee trainee = gymFacade.createTrainee(traineeRegistrationRequest);

        return new CredentialsDTO(
                trainee.getUsername(),
                trainee.getPassword()
        );
    }


    @GetMapping("/{username}")
    @ApiOperation(
            value = "Get trainee profile",
            notes = "Returns trainee profile information by username"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = AuthHttpHeader.USERNAME,
                    value = "Authenticated username",
                    required = true,
                    paramType = "header",
                    dataType = "string"
            ),
            @ApiImplicitParam(
                    name = AuthHttpHeader.PASSWORD,
                    value = "User password",
                    required = true,
                    paramType = "header",
                    dataType = "string"
            )
    })
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Profile found",
                    response = TraineeProfileResponse.class
            ),
            @ApiResponse(
                    code = 403,
                    message = "Forbidden"
            ),
            @ApiResponse(
                    code = 404,
                    message = "Trainee not found"
            )
    })
    public TraineeProfileResponse getTraineeByUsername(
            @ApiParam(
                    value = "Trainee username",
                    required = true
            )
            @PathVariable("username") String username
    ) {
        return gymFacade.getTrainee(username);
    }


    @PutMapping("/{username}")
    @ApiOperation(
            value = "Update trainee profile",
            notes = "Updates trainee personal information"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = AuthHttpHeader.USERNAME,
                    value = "Authenticated username",
                    required = true,
                    paramType = "header",
                    dataType = "string"
            ),
            @ApiImplicitParam(
                    name = AuthHttpHeader.PASSWORD,
                    value = "User password",
                    required = true,
                    paramType = "header",
                    dataType = "string"
            )
    })
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Trainee updated",
                    response = TraineeProfileResponse.class
            ),
            @ApiResponse(
                    code = 403,
                    message = "Forbidden"
            ),
            @ApiResponse(
                    code = 400,
                    message = "Validation error"
            )
    })
    public ResponseEntity<TraineeProfileResponse> updateTraineeByUsername(
            @ApiParam(
                    value = "Trainee username",
                    required = true
            )
            @PathVariable("username") String username,

            @RequestHeader(AuthHttpHeader.USERNAME) String authUsername,

            @Valid @RequestBody UpdateTraineeRequest request
    ) {
        if (!username.equals(authUsername)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(
                gymFacade.updateTrainee(username, request)
        );
    }

    @DeleteMapping("/{username}")
    @ApiOperation(
            value = "Delete trainee",
            notes = "Deletes trainee profile"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = AuthHttpHeader.USERNAME,
                    value = "Authenticated username",
                    required = true,
                    paramType = "header",
                    dataType = "string"
            ),
            @ApiImplicitParam(
                    name = AuthHttpHeader.PASSWORD,
                    value = "User password",
                    required = true,
                    paramType = "header",
                    dataType = "string"
            )
    })
    @ApiResponses({
            @ApiResponse(
                    code = 204,
                    message = "Trainee deleted"
            ),
            @ApiResponse(
                    code = 403,
                    message = "Forbidden"
            )
    })
    public ResponseEntity<Void> deleteTraineeByUsername(
            @ApiParam(
                    value = "Trainee username",
                    required = true
            )
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
    @ApiOperation(
            value = "Get unassigned trainers",
            notes = "Returns active trainers not assigned to trainee"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = AuthHttpHeader.USERNAME,
                    value = "Authenticated username",
                    required = true,
                    paramType = "header",
                    dataType = "string"
            ),
            @ApiImplicitParam(
                    name = AuthHttpHeader.PASSWORD,
                    value = "User password",
                    required = true,
                    paramType = "header",
                    dataType = "string"
            )
    })
    @ApiResponse(
            code = 200,
            message = "List of available trainers",
            response = TrainerSummaryResponse.class
    )
    public ResponseEntity<List<TrainerSummaryResponse>> getUnassignedTrainers(
            @ApiParam(
                    value = "Trainee username",
                    required = true
            )
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
    @ApiOperation(
            value = "Get trainee trainings",
            notes = "Returns trainee training history filtered by criteria"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = AuthHttpHeader.USERNAME,
                    value = "Authenticated username",
                    required = true,
                    paramType = "header",
                    dataType = "string"
            ),
            @ApiImplicitParam(
                    name = AuthHttpHeader.PASSWORD,
                    value = "User password",
                    required = true,
                    paramType = "header",
                    dataType = "string"
            )
    })
    @ApiResponse(
            code = 200,
            message = "Training list",
            response = TraineeTrainingResponse.class
    )
    public ResponseEntity<List<TraineeTrainingResponse>> getTrainings(
            @ApiParam(
                    value = "Trainee username",
                    required = true
            )
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
    @ApiOperation(
            value = "Update trainee trainers list",
            notes = "Assigns trainers to trainee"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = AuthHttpHeader.USERNAME,
                    value = "Authenticated username",
                    required = true,
                    paramType = "header",
                    dataType = "string"
            ),
            @ApiImplicitParam(
                    name = AuthHttpHeader.PASSWORD,
                    value = "User password",
                    required = true,
                    paramType = "header",
                    dataType = "string"
            )
    })
    @ApiResponse(
            code = 200,
            message = "Updated trainers list",
            response = TrainerSummaryResponse.class
    )
    public ResponseEntity<List<TrainerSummaryResponse>> updateTraineeTrainersList(
            @ApiParam(
                    value = "Trainee username",
                    required = true
            )
            @PathVariable("username") String username,

            @RequestHeader(AuthHttpHeader.USERNAME) String authUsername,

            @Valid @RequestBody UpdateTraineeTrainerListRequest request
    ) {
        if (!username.equals(authUsername)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(
                gymFacade.updateTrainersListByTraineeUsername(
                        username,
                        request
                )
        );
    }


    @PatchMapping("/{username}/activate")
    @ApiOperation(
            value = "Activate or deactivate trainee",
            notes = "Changes trainee active status"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = AuthHttpHeader.USERNAME,
                    value = "Authenticated username",
                    required = true,
                    paramType = "header",
                    dataType = "string"
            ),
            @ApiImplicitParam(
                    name = AuthHttpHeader.PASSWORD,
                    value = "User password",
                    required = true,
                    paramType = "header",
                    dataType = "string"
            )
    })
    @ApiResponses({
            @ApiResponse(
                    code = 204,
                    message = "Status updated"
            ),
            @ApiResponse(
                    code = 403,
                    message = "Forbidden"
            )
    })
    public ResponseEntity<Void> activateTrainee(
            @ApiParam(
                    value = "Trainee username",
                    required = true
            )
            @PathVariable("username") String username,

            @RequestHeader(AuthHttpHeader.USERNAME) String authUsername,

            @Valid @RequestBody ActivateUserRequest request
    ) {
        if (!username.equals(authUsername)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        gymFacade.updateActiveStatusByUsername(
                username,
                request.isActive()
        );

        return ResponseEntity.noContent().build();
    }
}