package io.github.George_Al3xander.controller;

import io.github.George_Al3xander.dto.auth.CredentialsDTO;
import io.github.George_Al3xander.dto.filter.TrainingFilter;
import io.github.George_Al3xander.dto.trainer.TrainerProfileResponse;
import io.github.George_Al3xander.dto.trainer.TrainerRegistrationRequest;
import io.github.George_Al3xander.dto.trainer.TrainerTrainingResponse;
import io.github.George_Al3xander.dto.trainer.UpdateTrainerRequest;
import io.github.George_Al3xander.dto.user.ActivateUserRequest;
import io.github.George_Al3xander.facade.GymFacade;
import io.github.George_Al3xander.web.AuthHttpHeader;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/trainers")
@RequiredArgsConstructor
@Api(tags = "Trainer Management", description = "Operations related to trainer profiles and trainings")
public class TrainerController {

    private final GymFacade gymFacade;


    @PostMapping
    @ApiOperation(
            value = "Create trainer",
            notes = "Registers a new trainer and returns generated credentials"
    )
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Trainer successfully created",
                    response = CredentialsDTO.class
            ),
            @ApiResponse(
                    code = 400,
                    message = "Validation error"
            )
    })
    public CredentialsDTO createTrainer(
            @ApiParam(
                    value = "Trainer registration data",
                    required = true
            )
            @Valid @RequestBody TrainerRegistrationRequest trainerRegistrationRequest
    ) {

        return gymFacade.createTrainer(trainerRegistrationRequest);
    }


    @GetMapping("/{username}")
    @ApiOperation(
            value = "Get trainer profile",
            notes = "Returns trainer profile information by username"
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
                    message = "Trainer profile found",
                    response = TrainerProfileResponse.class
            ),
            @ApiResponse(
                    code = 403,
                    message = "Forbidden"
            ),
            @ApiResponse(
                    code = 404,
                    message = "Trainer not found"
            )
    })
    public TrainerProfileResponse getTrainerByUsername(
            @ApiParam(
                    value = "Trainer username",
                    required = true
            )
            @PathVariable("username") String username
    ) {
        return gymFacade.getTrainer(username);
    }


    @PutMapping("/{username}")
    @ApiOperation(
            value = "Update trainer profile",
            notes = "Updates trainer personal information"
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
                    message = "Trainer updated successfully",
                    response = TrainerProfileResponse.class
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
    public ResponseEntity<TrainerProfileResponse> updateTrainerByUsername(
            @ApiParam(
                    value = "Trainer username",
                    required = true
            )
            @PathVariable("username") String username,

            @RequestHeader(AuthHttpHeader.USERNAME) String authUsername,

            @ApiParam(
                    value = "Trainer update data",
                    required = true
            )
            @Valid @RequestBody UpdateTrainerRequest request
    ) {

        return ResponseEntity.ok(
                gymFacade.updateTrainer(username, request)
        );
    }


    @GetMapping("/{username}/trainings")
    @ApiOperation(
            value = "Get trainer trainings",
            notes = "Returns trainer training history filtered by criteria"
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
                    message = "Training list returned",
                    response = TrainerTrainingResponse.class
            ),
            @ApiResponse(
                    code = 403,
                    message = "Forbidden"
            )
    })
    public ResponseEntity<List<TrainerTrainingResponse>> getTrainings(
            @ApiParam(
                    value = "Trainer username",
                    required = true
            )
            @PathVariable("username") String username,

            @RequestHeader(AuthHttpHeader.USERNAME) String authUsername,

            @ApiParam(
                    value = "Training filter criteria",
                    required = true
            )
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
    @ApiOperation(
            value = "Activate or deactivate trainer",
            notes = "Changes trainer active status"
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
                    message = "Status updated successfully"
            ),
            @ApiResponse(
                    code = 403,
                    message = "Forbidden"
            )
    })
    public ResponseEntity<Void> activateTrainer(
            @ApiParam(
                    value = "Trainer username",
                    required = true
            )
            @PathVariable("username") String username,

            @RequestHeader(AuthHttpHeader.USERNAME) String authUsername,

            @ApiParam(
                    value = "Activation status request",
                    required = true
            )
            @Valid @RequestBody ActivateUserRequest request
    ) {
        
        gymFacade.updateActiveStatusByUsername(
                username,
                request.isActive()
        );

        return ResponseEntity.noContent().build();
    }
}