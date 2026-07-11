package com.gym.services;

import com.gym.clients.TrainerWorkloadClient;
import com.gym.dto.requests.TrainingWorkloadRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

//service for trainer workload business logic
@Service
public class TrainerWorkloadService {
    private static final Logger log = LoggerFactory.getLogger(TrainerWorkloadService.class);

    private final TrainerWorkloadClient client;

    public TrainerWorkloadService(TrainerWorkloadClient client) {
        this.client = client;
    }

    @CircuitBreaker(
            name = "workloadService",
            fallbackMethod = "workloadFallback"
    )
    public void updateWorkload(TrainingWorkloadRequest request) {
        client.updateWorkload(request);

        log.info("Trainer workload updated for {}", request.getTrainerUsername());
    }


    public void workloadFallback(TrainingWorkloadRequest request, Throwable throwable) {
        log.error(
                "Trainer workload service unavailable. Trainer={}, error={}",
                request.getTrainerUsername(),
                throwable.getMessage()
        );
    }
}
