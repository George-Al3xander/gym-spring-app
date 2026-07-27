package io.github.George_Al3xander.controller;

import io.github.George_Al3xander.dao.TrainingTypeDao;
import io.github.George_Al3xander.dto.TrainingTypeResponse;
import io.github.George_Al3xander.model.TrainingType;
import io.github.George_Al3xander.web.AuthHttpHeader;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/training-types")
@RequiredArgsConstructor
@Api(tags = "Training Type Management", description = "Operations related to available training types")
public class TrainingTypeController {

    private final TrainingTypeDao trainingTypeDao;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(
            value = "Get training types",
            notes = "Returns all available training types"
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
                    message = "Training types successfully retrieved",
                    response = TrainingTypeResponse.class
            ),
            @ApiResponse(
                    code = 401,
                    message = "Authentication failed"
            )
    })
    public List<TrainingTypeResponse> getTrainingTypes() {
        List<TrainingType> trainingTypes = trainingTypeDao.findAll();

        return trainingTypes.stream()
                .map(tt ->
                        new TrainingTypeResponse(
                                tt.getId(),
                                tt.getTrainingTypeName()
                        )
                ).toList();
    }
}