package com.gym.steps;

import com.gym.dao.TraineeDao;
import com.gym.dao.TrainerDao;
import com.gym.dao.TrainingTypeDao;
import com.gym.models.*;
import com.gym.services.TrainingService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

//cucumber step definition class
public class TrainingSteps {
    @Autowired
    private TrainingService trainingService;

    @Autowired
    private TraineeDao traineeDao;

    @Autowired
    private TrainerDao trainerDao;

    @Autowired
    private TrainingTypeDao trainingTypeDao;

    private Training training;
    private Exception exception;

    //matches the given step in the feature file
    @Given("trainee {string} and trainer {string} exist")
    public void usersExist(String traineeUsername, String trainerUsername) {
        Trainee trainee = traineeDao.findByUsername(traineeUsername);

        if (trainee == null) {
            User traineeUser = new User();
            traineeUser.setUsername(traineeUsername);
            traineeUser.setFirstName("John");
            traineeUser.setLastName("Doe");
            traineeUser.setPassword("test");
            traineeUser.setActive(true);

            trainee = new Trainee();
            trainee.setUser(traineeUser);

            traineeDao.save(trainee);
        }

        Trainer trainer = trainerDao.findByUsername(trainerUsername);

        if (trainer == null) {
            User trainerUser = new User();
            trainerUser.setUsername(trainerUsername);
            trainerUser.setFirstName("Michael");
            trainerUser.setLastName("Jordan");
            trainerUser.setPassword("test");
            trainerUser.setActive(true);

            trainer = new Trainer();
            trainer.setUser(trainerUser);
            trainer.setSpecialization("Basketball");

            trainerDao.save(trainer);
        }

        TrainingType trainingType; //variable that will contain the training type

        try {
            trainingType = trainingTypeDao.findByName("Basketball");
        } catch (RuntimeException e) {
            trainingType = new TrainingType();
            trainingType.setTrainingTypeName("Basketball");
            trainingTypeDao.save(trainingType);
        }

        training = new Training();
        training.setTrainee(trainee);
        training.setTrainer(trainer);
        training.setTrainingType(trainingType);
        training.setTrainingDate(LocalDate.now());

        exception = null;
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
    public void trainingCreated() {
        assertNull(exception); //verifies that no exception was thrown
    }

    //matches the failure verification step
    @Then("training creation should fail")
    public void trainingFailed(){
        assertNotNull(exception); //verifies that an exception was thrown
    }

    @When("trainer is missing")
    public void trainerIsMissing() {

        training.setTrainer(null);

        try {
            trainingService.create(training);
        } catch (Exception e) {
            exception = e;
        }
    }

    @When("trainee is missing")
    public void traineeIsMissing() {

        training.setTrainee(null);

        try {
            trainingService.create(training);
        } catch (Exception e) {
            exception = e;
        }
    }

    @When("training type is missing")
    public void trainingTypeIsMissing() {

        training.setTrainingType(null);

        try {
            trainingService.create(training);
        } catch (Exception e) {
            exception = e;
        }
    }

    @When("training date is missing")
    public void trainingDateIsMissing() {

        training.setTrainingDate(null);

        try {
            trainingService.create(training);
        } catch (Exception e) {
            exception = e;
        }
    }
}