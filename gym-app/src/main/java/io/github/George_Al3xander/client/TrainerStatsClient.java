package io.github.George_Al3xander.client;

import io.github.George_Al3xander.dto.trainer.TrainerWorkloadRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("trainer-stats-app")
public interface TrainerStatsClient {
    @PostMapping("trainer/workload")
    void addTrainingWorkload(@RequestBody TrainerWorkloadRequest request);
}
