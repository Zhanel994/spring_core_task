package com.gym.services;

import com.gym.clients.TrainerWorkloadClient;
import com.gym.dao.TrainingDao;
import com.gym.dto.requests.TrainingWorkloadRequest;
import com.gym.exceptions.ValidationException;
import com.gym.models.Training;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

//service for training business logic
@Service
@Transactional
public class TrainingService {
    private static final Logger log = LoggerFactory.getLogger(TrainingService.class);

    private final TrainingDao trainingDao;
    private final TrainerWorkloadService trainerWorkloadService;

    public TrainingService(TrainingDao trainingDao, TrainerWorkloadService trainerWorkloadService) {
        this.trainingDao = trainingDao;
        this.trainerWorkloadService = trainerWorkloadService;
    }

    //creates new training
    public Training create(Training training) {
        if (training == null) {
            throw new ValidationException("Training required!");
        }

        if (training.getTrainee() == null) {
            throw new ValidationException("Trainee required!");
        }

        if (training.getTrainer() == null) {
            throw new ValidationException("Trainer required!");
        }

        if (training.getTrainingType() == null) {
            throw new ValidationException("Training type required!");
        }

        if (training.getTrainingName() == null || training.getTrainingName().isBlank()) {
            throw new ValidationException("Training name required!");
        }

        if (training.getDuration() <= 0) {
            throw new ValidationException("Duration must be > 0!");
        }

        if (training.getTrainingDate() == null) {
            throw new ValidationException("Training date required!");
        }

        trainingDao.save(training);

        TrainingWorkloadRequest request = new TrainingWorkloadRequest();

        request.setTrainerUsername(training.getTrainer().getUser().getUsername());
        request.setTrainerFirstName(training.getTrainer().getUser().getFirstName());
        request.setTrainerLastName(training.getTrainer().getUser().getLastName());
        request.setActive(training.getTrainer().getUser().isActive());
        request.setTrainingDate(training.getTrainingDate());
        request.setTrainingDuration(training.getDuration());

        request.setActionType("ADD");

        trainerWorkloadService.updateWorkload(request);

        log.info("Training created: {}", training.getTrainingName());

        return training;
    }

    //gets training by id
    public Training get(Long id) {
        log.info("Training fetched: id={}", id);

        return trainingDao.findById(id);
    }

    //gets trainings by trainee
    public List<Training> getTraineeTrainings(String username, LocalDate from, LocalDate to,
                                              String trainerName, String trainingType) {
        log.info("Fetching trainee trainings for username={}, from={}, to={}, trainerName={}, trainingType={}",
                username, from, to, trainerName, trainingType);

        List<Training> trainings = trainingDao.findTraineeTrainings(
                username, from, to, trainerName, trainingType
        );

        log.info("Found {} trainings for trainee {}", trainings.size(), username);

        return trainings;
    }

    //gets trainings by trainer
    public List<Training> getTrainerTrainings(String username, LocalDate from, LocalDate to, String traineeName) {
        log.info("Fetching trainer trainings for username={}, from={}, to={}, traineeName={}",
                username, from, to, traineeName);

        List<Training> trainings = trainingDao.findTrainerTrainings(
                username, from, to, traineeName
        );

        log.info("Found {} trainings for trainer {}", trainings.size(), username);

        return trainings;
    }
}