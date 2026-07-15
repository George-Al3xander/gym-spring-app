package io.github.George_Al3xander.controller;

import io.github.George_Al3xander.dto.CredentialsDTO;
import io.github.George_Al3xander.dto.trainer.TrainerProfileResponse;
import io.github.George_Al3xander.dto.trainer.TrainerRegistrationRequest;
import io.github.George_Al3xander.facade.GymFacade;
import io.github.George_Al3xander.model.Trainer;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/trainers")
@RequiredArgsConstructor
public class TrainerController {

    private final GymFacade gymFacade;

    @PostMapping
    public CredentialsDTO createTrainer(
            @Valid @RequestBody TrainerRegistrationRequest trainerRegistrationRequest
    ) {
        Trainer trainer = gymFacade.createTrainer(trainerRegistrationRequest);

        return new CredentialsDTO(trainer.getUsername(), trainer.getPassword());
    }

    @GetMapping("/{username}")
    public TrainerProfileResponse getTrainerByUsername(@PathVariable("username") String username) {
        return gymFacade.getTrainer(username);
    }
}
