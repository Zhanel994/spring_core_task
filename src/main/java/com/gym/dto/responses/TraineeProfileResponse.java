package com.gym.dto.responses;

import java.time.LocalDate;
import java.util.List;

public class TraineeProfileResponse {
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String address;
    private boolean active;

    private List<TrainerInfoResponse> trainers;

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

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<TrainerInfoResponse> getTrainers() {
        return trainers;
    }

    public void setTrainers(List<TrainerInfoResponse> trainers) {
        this.trainers = trainers;
    }
}
