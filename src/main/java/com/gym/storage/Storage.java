package com.gym.storage;

import com.gym.models.Trainee;
import com.gym.models.Trainer;
import com.gym.models.Training;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class Storage {
    private Map<Long, Trainee> trainees = new HashMap<>();
    private Map<Long, Trainer> trainers = new HashMap<>();
    private Map<Long, Training> trainings = new HashMap<>();

    private Long traineeIdSeq = 0L;
    private Long trainerIdSeq = 0L;
    private Long trainingIdSeq = 0L;

    public Long generateTraineeId() {
        return ++traineeIdSeq;
    }

    public Long generateTrainerId() {
        return ++trainerIdSeq;
    }

    public Long generateTrainingId() {
        return ++trainingIdSeq;
    }

    public Map<Long, Trainee> getTrainees() {
        return trainees;
    }

    public Map<Long, Trainer> getTrainers() {
        return trainers;
    }

    public Map<Long, Training> getTrainings() {
        return trainings;
    }

    public boolean usernameExists(String username) {
        return trainees.values().stream()
                .anyMatch(t -> username.equals(t.getUsername()))
                || trainers.values().stream()
                .anyMatch(t -> username.equals(t.getUsername()));
    }
}
