package com.gym.facade;

import com.gym.dto.responses.RegistrationResponse;
import com.gym.models.Trainee;
import com.gym.models.Trainer;
import com.gym.models.Training;
import com.gym.services.TraineeService;
import com.gym.services.TrainerService;
import com.gym.services.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//facade for gym operations to provide simplifies access to services
@Component
public class GymFacade {
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;

    @Autowired
    public GymFacade(TraineeService traineeService, TrainerService trainerService, TrainingService trainingService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
    }

    public RegistrationResponse createTrainee(Trainee trainee){
        return traineeService.create(trainee);
    }

    public RegistrationResponse createTrainer(Trainer trainer){
        return trainerService.create(trainer);
    }

    public Training addTraining(Training training){
        return trainingService.create(training);
    }

}
