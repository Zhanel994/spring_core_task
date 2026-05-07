package com.gym;

import com.gym.config.AppConfig;
import com.gym.models.Trainee;
import com.gym.services.TraineeService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(AppConfig.class);

        TraineeService traineeService = context.getBean(TraineeService.class);

        Trainee trainee = new Trainee();
        trainee.setFirstName("John");
        trainee.setLastName("Smith");

        Trainee created = traineeService.create(trainee);

        System.out.println("Created trainee:");
        System.out.println("ID: " + created.getId());
        System.out.println("Username: " + created.getUsername());
        System.out.println("Password: " + created.getPassword());

        context.close();
    }
}