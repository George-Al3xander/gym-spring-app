package io.github.George_Al3xander.controller;

import io.github.George_Al3xander.dto.workload.WorkloadRequest;
import io.github.George_Al3xander.model.TrainerWorkload;
import io.github.George_Al3xander.service.TrainerWorkloadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trainer")
@RequiredArgsConstructor
public class TrainerController {

    private final TrainerWorkloadService trainerWorkloadService;

    @PostMapping("/workload")
    public ResponseEntity<Void> addTraining(
            @Valid @RequestBody WorkloadRequest request
    ) {
        trainerWorkloadService.handleTraining(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/workload/{username}")
    public ResponseEntity<TrainerWorkload> getWorkload(
            @PathVariable(name = "username") String username
    ) {
        TrainerWorkload workload = trainerWorkloadService.getWorkloadByTrainerUsername(username);

        return ResponseEntity.ok(workload);
    }

}
