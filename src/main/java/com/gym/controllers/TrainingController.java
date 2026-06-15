package com.gym.controllers;

import com.gym.dao.TrainingTypeDao;
import com.gym.dto.requests.TrainingRequest;
import com.gym.dto.responses.TrainingResponse;
import com.gym.models.Training;
import com.gym.models.TrainingType;
import com.gym.services.TraineeService;
import com.gym.services.TrainerService;
import com.gym.services.TrainingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

//REST Controller for trainings
@RestController
@RequestMapping("api/v1")
@Tag(name = "Training API")
public class TrainingController {
    private final TrainingService trainingService;
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingTypeDao trainingTypeDao;

    public TrainingController(TrainingService trainingService, TrainerService trainerService, TraineeService traineeService, TrainingTypeDao trainingTypeDao) {
        this.trainingService = trainingService;
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingTypeDao = trainingTypeDao;
    }

    //returns trainee trainings
    @GetMapping("/trainee/trainings")
    public List<TrainingResponse> getTraineeTrainings(@RequestParam String username,
                                                      @RequestParam(required = false) LocalDate from,
                                                      @RequestParam(required = false) LocalDate to,
                                                      @RequestParam(required = false) String trainerName,
                                                      @RequestParam(required = false) String trainingType) {

        return trainingService.getTraineeTrainings(username, from, to, trainerName, trainingType).stream()
                .map(training -> {
                    TrainingResponse response = new TrainingResponse();
                    response.setTrainingName(training.getTrainingName());
                    response.setTrainingDate(training.getTrainingDate());
                    response.setDuration(training.getDuration());
                    response.setTrainingType(training.getTrainingType().getTrainingTypeName());
                    response.setTrainerName(
                            training.getTrainer().getUser().getFirstName() + " " +
                                    training.getTrainer().getUser().getLastName()
                    );
                    return response;
                })
                .collect(Collectors.toList());
    }

    //returns trainer trainings
    @GetMapping("trainer/trainings")
    public List<TrainingResponse> getTrainerTrainings(@RequestParam String username,
                                                      @RequestParam(required = false) LocalDate from,
                                                      @RequestParam(required = false) LocalDate to,
                                                      @RequestParam(required = false) String traineeName) {
        return trainingService.getTrainerTrainings(username, from, to, traineeName).stream()
                                                                    .map(training -> {
                                                                        TrainingResponse response = new TrainingResponse();
                                                                        response.setTrainingName(training.getTrainingName());
                                                                        response.setTrainingDate(training.getTrainingDate());
                                                                        response.setDuration(training.getDuration());
                                                                        response.setTrainingType(training.getTrainingType().getTrainingTypeName());
                                                                        response.setTrainerName(
                                                                                training.getTrainee().getUser().getFirstName() + " " +
                                                                                        training.getTrainee().getUser().getLastName()
                                                                        );
                                                                        return response;
                                                                    })
                                                                    .collect(Collectors.toList());
    }

    //creates a new training
    @PostMapping("/trainings")
    public ResponseEntity<String> addTraining(@RequestBody TrainingRequest request) {

        Training training = new Training();

        training.setTrainee(traineeService.getByUsername(request.getTraineeUsername()));
        training.setTrainer(trainerService.getByUsername(request.getTrainerUsername()));
        training.setTrainingName(request.getTrainingName());
        training.setTrainingDate(request.getTrainingDate());
        training.setDuration(request.getDuration());
        training.setTrainingType(trainingTypeDao.findByName(request.getTrainingType()));

        trainingService.create(training);

        return ResponseEntity.ok("Training added successfully");
    }
}
