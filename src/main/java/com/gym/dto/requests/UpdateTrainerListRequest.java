package com.gym.dto.requests;

import java.util.List;

public class UpdateTrainerListRequest {
    private String traineeUsername;
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
