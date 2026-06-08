package com.gym.controllers;

import com.gym.dto.requests.TraineeRegistrationRequest;
import com.gym.dto.requests.UpdateTraineeRequest;
import com.gym.dto.requests.UpdateTrainerListRequest;
import com.gym.dto.responses.RegistrationResponse;
import com.gym.dto.responses.TraineeProfileResponse;
import com.gym.dto.responses.TrainerInfoResponse;
import com.gym.models.Trainee;
import com.gym.models.User;
import com.gym.services.TraineeService;
import com.gym.services.TrainingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

//REST Controller for trainees
@RestController
@RequestMapping("api/v1/trainees")
@Api(tags = "Trainee API")
public class TraineeController {
    private final TraineeService traineeService;
    private final TrainingService trainingService;

    public TraineeController(TraineeService traineeService, TrainingService trainingService) {
        this.traineeService = traineeService;
        this.trainingService = trainingService;
    }

    //registers a new trainee
    @PostMapping("/register")
    @ApiOperation("Register trainee")
    public RegistrationResponse register(@RequestBody TraineeRegistrationRequest registrationRequest) {
        User user = new User();
        user.setFirstName(registrationRequest.getFirstName());
        user.setLastName(registrationRequest.getLastName());

        Trainee trainee = new Trainee();
        trainee.setUser(user);
        trainee.setDateOfBirth(registrationRequest.getDateOfBirth());
        trainee.setAddress(registrationRequest.getAddress());

        Trainee created = traineeService.create(trainee);

        return new RegistrationResponse(created.getUser().getUsername(), created.getUser().getPassword());
    }

    //returns trainee profile by username
    @GetMapping("/{username}")
    @ApiOperation("Get trainee profile")
    public TraineeProfileResponse getProfile(@PathVariable String username) {
        Trainee trainee = traineeService.getByUsername(username);

        TraineeProfileResponse traineeProfileResponse = new TraineeProfileResponse();

        traineeProfileResponse.setFirstName(trainee.getUser().getFirstName());
        traineeProfileResponse.setLastName(trainee.getUser().getLastName());
        traineeProfileResponse.setDateOfBirth(trainee.getDateOfBirth());
        traineeProfileResponse.setAddress(trainee.getAddress());
        traineeProfileResponse.setActive(trainee.getUser().isActive());

        traineeProfileResponse.setTrainers(
                trainee.getTrainers().stream()
                        .map(trainer -> {
                            TrainerInfoResponse dto = new TrainerInfoResponse();
                            dto.setUsername(trainer.getUser().getUsername());
                            dto.setFirstName(trainer.getUser().getFirstName());
                            dto.setLastName(trainer.getUser().getLastName());
                            dto.setSpecialization(trainer.getSpecialization());
                            return dto;
                        })
                        .collect(Collectors.toList())
        );
        return traineeProfileResponse;
    }

    //updates trainee
    @PutMapping
    @ApiOperation("Update trainee")
    public TraineeProfileResponse update(@RequestBody UpdateTraineeRequest updateTraineeRequest) {
        Trainee trainee = traineeService.getByUsername(updateTraineeRequest.getUsername());

        trainee.getUser().setFirstName(updateTraineeRequest.getFirstName());
        trainee.getUser().setLastName(updateTraineeRequest.getLastName());
        trainee.getUser().setActive(updateTraineeRequest.isActive());
        trainee.setDateOfBirth(updateTraineeRequest.getDateOfBirth());
        trainee.setAddress(updateTraineeRequest.getAddress());

        traineeService.update(trainee);

        return getProfile(updateTraineeRequest.getUsername());
    }

    //deletes trainee by username
    @DeleteMapping("/{username}")
    @ApiOperation("Delete trainee")
    public ResponseEntity<String> delete(@PathVariable String username) {
        traineeService.deleteByUsername(username);

        return ResponseEntity.ok("Trainee deleted");
    }

    //returns trainers that are not assigned to the trainee
    @GetMapping("/{username}/unassigned-trainers")
    public List<TrainerInfoResponse> getUnassigned(@PathVariable String username) {

        return traineeService.getUnassigned(username).stream()
                .filter(trainer -> trainer.getUser().isActive())
                .map(trainer -> {
                    TrainerInfoResponse dto = new TrainerInfoResponse();

                    dto.setUsername(trainer.getUser().getUsername());
                    dto.setFirstName(trainer.getUser().getFirstName());
                    dto.setLastName(trainer.getUser().getLastName());
                    dto.setSpecialization(trainer.getSpecialization());

                    return dto;
                })
                .collect(Collectors.toList());
    }

    //updates trainer list
    @PutMapping("/trainer-list")
    public List<TrainerInfoResponse> updateTrainerList(@RequestBody UpdateTrainerListRequest request) {
        traineeService.updateTrainerList(request.getTraineeUsername(), request.getTrainerIds());

        Trainee trainee = traineeService.getByUsername(request.getTraineeUsername());

        return trainee.getTrainers().stream()
                .map(trainer -> {
                    TrainerInfoResponse dto = new TrainerInfoResponse();

                    dto.setUsername(trainer.getUser().getUsername());
                    dto.setFirstName(trainer.getUser().getFirstName());
                    dto.setLastName(trainer.getUser().getLastName());
                    dto.setSpecialization(trainer.getSpecialization());

                    return dto;
                })
                .collect(Collectors.toList());
    }

    //changes status by username
    @PatchMapping("/{username}/status")
    public ResponseEntity<String> changeTraineeStatus(@RequestParam String username, @RequestParam boolean isActive) {
        Trainee trainee = traineeService.getByUsername(username);
        trainee.getUser().setActive(isActive);
        traineeService.update(trainee);

        return ResponseEntity.ok("Trainee status updated");
    }
}
