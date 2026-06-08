package com.gym.dto.responses;

import java.util.List;

public class TrainerProfileResponse {
    private String username;
    private String firstName;
    private String lastName;
    private String specialization;
    private boolean active;

    private List<TraineeInfoResponse> trainees;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<TraineeInfoResponse> getTrainees() {
        return trainees;
    }

    public void setTrainees(List<TraineeInfoResponse> trainees) {
        this.trainees = trainees;
    }
}
