package com.gym.steps;

import com.gym.models.Trainee;
import com.gym.models.Trainer;
import com.gym.models.Training;
import com.gym.services.TrainingService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

//cucumber step definition class
public class TrainingSteps {
    @Autowired
    private TrainingService trainingService;

    private Training training;
    private Exception exception;


    //matches the given step in the feature file
    @Given("trainee {string} and trainer {string} exist")
    public void usersExist(String traineeUsername, String trainerUsername) {
        Trainee trainee = new Trainee();
        Trainer trainer = new Trainer();

        training = new Training();

        training.setTrainee(trainee);
        training.setTrainer(trainer);
    }


    //matches the when step
    @When("I create a training with name {string} and duration {int}")
    public void createTraining(String name, int duration) {
        training.setTrainingName(name);
        training.setDuration(duration);

        try {
            trainingService.create(training);
        }
        catch(Exception exc){
            exception = exc;
        }
    }

    //matches the success verification step
    @Then("training should be created successfully")
    public void trainingCreated(){
        assertNull(exception); //verifies that no exception was thrown
    }


    //matches the failure verification step
    @Then("training creation should fail")
    public void trainingFailed(){
        assertNotNull(exception); //verifies that an exception was thrown
    }
}
