package io.github.George_Al3xander.controller;

import io.github.George_Al3xander.dao.TrainingTypeDao;
import io.github.George_Al3xander.dto.TrainingTypeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/training-types")
@RequiredArgsConstructor
@Tag(
        name = "Training Type Management",
        description = "Operations related to available training types"
)
public class TrainingTypeController {

    private final TrainingTypeDao trainingTypeDao;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Get training types",
            description = "Returns all available training types"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Training types successfully retrieved",
                    content = @Content(
                            array = @ArraySchema(
                                    schema = @Schema(implementation = TrainingTypeResponse.class)
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentication failed"
            )
    })
    public List<TrainingTypeResponse> getTrainingTypes() {

        return trainingTypeDao.findAll()
                .stream()
                .map(tt -> new TrainingTypeResponse(
                        tt.getId(),
                        tt.getTrainingTypeName()
                ))
                .toList();
    }
}