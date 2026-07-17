package io.github.George_Al3xander.controller;

import io.github.George_Al3xander.dao.TrainingTypeDao;
import io.github.George_Al3xander.dto.TrainingTypeResponse;
import io.github.George_Al3xander.model.TrainingType;
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
public class TrainingTypeController {

    private final TrainingTypeDao trainingTypeDao;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TrainingTypeResponse> getTrainingTypes() {
        List<TrainingType> trainingTypes = trainingTypeDao.findAll();

        return trainingTypes.stream()
                .map(tt ->
                        new TrainingTypeResponse(
                                tt.getId(), tt.getTrainingTypeName()
                        )
                ).toList();
    }
}
