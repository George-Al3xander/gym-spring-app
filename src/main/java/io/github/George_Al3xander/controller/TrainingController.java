package io.github.George_Al3xander.controller;

import io.github.George_Al3xander.dto.training.AddTrainingRequest;
import io.github.George_Al3xander.facade.GymFacade;
import io.github.George_Al3xander.web.AuthHttpHeader;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/trainings")
@RequiredArgsConstructor
@Api(tags = "Training Management", description = "Operations related to adding trainings")
public class TrainingController {

    private final GymFacade gymFacade;


    @PostMapping
    @ApiOperation(
            value = "Add training",
            notes = "Creates a new training session between trainee and trainer"
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
                    code = 201,
                    message = "Training successfully created"
            ),
            @ApiResponse(
                    code = 400,
                    message = "Validation error"
            ),
            @ApiResponse(
                    code = 401,
                    message = "Authentication failed"
            ),
            @ApiResponse(
                    code = 404,
                    message = "Trainee, trainer, or training type not found"
            )
    })
    public ResponseEntity<Void> addTraining(
            @ApiParam(
                    value = "Training creation data",
                    required = true
            )
            @Valid @RequestBody AddTrainingRequest request
    ) {
        gymFacade.addTraining(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}