package com.gym.controllers;

import com.gym.dto.requests.TrainerRegistrationRequest;
import com.gym.dto.requests.UpdateTrainerRequest;
import com.gym.dto.responses.RegistrationResponse;
import com.gym.dto.responses.TraineeInfoResponse;
import com.gym.dto.responses.TrainerProfileResponse;
import com.gym.models.Trainer;
import com.gym.models.User;
import com.gym.services.TrainerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

//REST Controller for trainers
@RestController
@RequestMapping("api/v1/trainers")
@Tag(name = "Trainer API")
public class TrainerController {
    private final TrainerService trainerService;

    public TrainerController(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    //registers a new trainer
    @PostMapping("/register")
    @Operation(
            summary = "Trainer Registration",
            description = "Creates trainer user",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Trainer registered successfully")
            }
    )
    public RegistrationResponse register(@Valid @RequestBody TrainerRegistrationRequest registrationRequest) {
        User user = new User();
        user.setFirstName(registrationRequest.getFirstName());
        user.setLastName(registrationRequest.getLastName());

        Trainer trainer = new Trainer();
        trainer.setUser(user);
        trainer.setSpecialization(registrationRequest.getSpecialization());

        Trainer created = trainerService.create(trainer);

        return new RegistrationResponse(created.getUser().getUsername(), created.getUser().getPassword());
    }

    //returns trainer profile by username
    @GetMapping("/{username}")
    public TrainerProfileResponse getProfile(@PathVariable String username) {
        Trainer trainer = trainerService.getByUsername(username);

        TrainerProfileResponse response = new TrainerProfileResponse();

        response.setUsername(trainer.getUser().getUsername());
        response.setFirstName(trainer.getUser().getFirstName());
        response.setLastName(trainer.getUser().getLastName());
        response.setSpecialization(trainer.getSpecialization());
        response.setActive(trainer.getUser().isActive());

        response.setTrainees(trainer.getTrainees().stream()
                                                    .map(trainee -> {
                                                                TraineeInfoResponse dto = new TraineeInfoResponse();
                                                                dto.setUsername(trainee.getUser().getUsername());
                                                                dto.setFirstName(trainee.getUser().getFirstName());
                                                                dto.setLastName(trainee.getUser().getLastName());
                                                                return dto;
                                                            })
                                                    .collect(Collectors.toList()));

        return response;
    }

    //updates trainer info
    @PutMapping
    public TrainerProfileResponse update(@Valid @RequestBody UpdateTrainerRequest request) {

        Trainer trainer = trainerService.getByUsername(request.getUsername());

        trainer.getUser().setFirstName(request.getFirstName());
        trainer.getUser().setLastName(request.getLastName());
        trainer.getUser().setActive(request.isActive());

        trainerService.update(trainer);

        return getProfile(request.getUsername());
    }

    //changes trainer status
    @PatchMapping("/{username}/status")
    public ResponseEntity<String> changeTrainerStatus(@PathVariable String username, @RequestParam boolean isActive) {
        Trainer trainer = trainerService.getByUsername(username);
        trainer.getUser().setActive(isActive);
        trainerService.update(trainer);

        return ResponseEntity.ok("Trainer status updated");
    }
}
