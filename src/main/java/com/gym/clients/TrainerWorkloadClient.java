package com.gym.clients;

import com.gym.config.FeignJwtConfig;
import org.springframework.cloud.openfeign.FeignClient;
import com.gym.dto.requests.TrainingWorkloadRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

//to work with another microservice
@FeignClient(
        name = "trainer-workload-service",
        configuration = FeignJwtConfig.class
)
public interface TrainerWorkloadClient {
    @PostMapping("/api/v1/workload")
    void updateWorkload(@RequestBody TrainingWorkloadRequest request);
}
