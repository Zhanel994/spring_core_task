package com.gym;

import com.gym.config.AppConfig;
import com.gym.models.Trainee;
import com.gym.models.User;
import com.gym.services.TraineeService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

//application entry point
public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(AppConfig.class);

        TraineeService traineeService = context.getBean(TraineeService.class);

        User user = new User();
        user.setFirstName("John");
        user.setLastName("Smith");

        Trainee trainee = new Trainee();
        trainee.setUser(user);

        Trainee created = traineeService.create(trainee);

        System.out.println(created.getUser().getUsername());
        System.out.println(created.getUser().getPassword());

        context.close();
    }
}