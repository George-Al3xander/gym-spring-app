package io.github.George_Al3xander.controller;

import io.github.George_Al3xander.dto.training.AddTrainingRequest;
import io.github.George_Al3xander.facade.GymFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/trainings")
@RequiredArgsConstructor
public class TrainingController {

    private final GymFacade gymFacade;

    @PostMapping
    public ResponseEntity<Void> addTraining(
            @Valid @RequestBody AddTrainingRequest request
    ) {
        gymFacade.addTraining(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
