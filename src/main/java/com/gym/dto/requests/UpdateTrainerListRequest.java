package com.gym.dto.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class UpdateTrainerListRequest {
    @NotBlank
    private String traineeUsername;

    @NotEmpty
    private List<Long> trainerIds;

    public String getTraineeUsername() {
        return traineeUsername;
    }

    public void setTraineeUsername(String traineeUsername) {
        this.traineeUsername = traineeUsername;
    }

    public List<Long> getTrainerIds() {
        return trainerIds;
    }

    public void setTrainerIds(List<Long> trainerIds) {
        this.trainerIds = trainerIds;
    }
}
