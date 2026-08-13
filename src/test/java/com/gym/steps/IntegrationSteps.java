package com.gym.steps;

import com.gym.dao.TraineeDao;
import com.gym.dao.TrainerDao;
import com.gym.dao.TrainingTypeDao;
import com.gym.models.Trainee;
import com.gym.models.Trainer;
import com.gym.models.TrainingType;
import com.gym.models.User;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

//cucumber step definition class
public class IntegrationSteps {
    //to work with REST services
    private final RestTemplate restTemplate = new RestTemplate();

    //the base of the workload service
    private final String workloadServiceUrl = "http://localhost:8081/api/v1/workload";

    @Autowired
    private TraineeDao traineeDao;

    @Autowired
    private TrainerDao trainerDao;

    @Autowired
    private TrainingTypeDao trainingTypeDao;

    //defines a cucumber given step
    @Given("trainer {string} has no workload")
    public void trainerHasNoWorkload(String username) {
        String url = workloadServiceUrl + "?username=" + username; //builds the URL for deleting the trainer's workload
        restTemplate.delete(url);
    }

    //defines a cucumber when step for creating a training
    @When("I create training for trainer {string} with duration {int}")
    public void createTraining(String trainerUsername, int duration) {
        createTraineeIfNeeded();
        createTrainerIfNeeded(trainerUsername);
        createTrainingTypeIfNeeded();

        String url = "http://localhost:8080/api/v1/trainings";

        //creates a json request body
        String request = """
                {
                    "traineeUsername": "John.Doe",
                    "trainerUsername": "%s",
                    "trainingName": "Basketball",
                    "trainingDate": "%s",
                    "duration": %d,
                    "trainingType": "Cardio"
                }
                """.formatted(
                trainerUsername,
                LocalDate.now(),
                duration
        );

        //to store HTTP headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON); //to specify that the request body contains json

        //json request body + http headers
        HttpEntity<String> entity = new HttpEntity<>(request, headers);

        restTemplate.postForEntity(
                url,
                entity,
                String.class
        );
    }

    //creates the trainee only if the trainee does not exist
    private void createTraineeIfNeeded() {
        //searches for a trainee with the username john.doe
        if (traineeDao.findByUsername("John.Doe") != null) {
            return;
        }

        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setUsername("John.Doe");
        user.setPassword("test");
        user.setActive(true);

        Trainee trainee = new Trainee();
        trainee.setUser(user);

        traineeDao.save(trainee);
    }

    //creates the trainer only if the trainee does not exist
    private void createTrainerIfNeeded(String username) {
        if (trainerDao.findByUsername(username) != null) {
            return;
        }

        User user = new User();
        user.setFirstName("Michael");
        user.setLastName("Jordan");
        user.setUsername(username);
        user.setPassword("test");
        user.setActive(true);

        Trainer trainer = new Trainer();
        trainer.setUser(user);
        trainer.setSpecialization("Basketball");

        trainerDao.save(trainer);
    }

    //creates the training type if necessary
    private void createTrainingTypeIfNeeded() {
        try {
            trainingTypeDao.findByName("Cardio");
        } catch (RuntimeException e) {
            TrainingType trainingType = new TrainingType();
            trainingType.setTrainingTypeName("Cardio");
        }
    }

    //defines a cucumber then step
    @Then("trainer workload should be updated to {int}")
    public void workloadShouldBeUpdated(int expected) {
        String url = workloadServiceUrl
                + "?username=Michael.Jordan"
                + "&year=" + LocalDate.now().getYear()
                + "&month=" + LocalDate.now().getMonthValue();

        //sends an HTTP get request to the workload service
        var response = restTemplate.getForEntity(
                url,
                WorkloadResponse.class
        );

        assertEquals(
                expected,
                response.getBody().trainingSummaryDuration()
        );
    }

    //defines an immutable data structure for the workload service response
    public record WorkloadResponse(
            String trainerUsername,
            String trainerFirstName,
            String trainerLastName,
            boolean active,
            int year,
            int month,
            int trainingSummaryDuration
    ) {}
}