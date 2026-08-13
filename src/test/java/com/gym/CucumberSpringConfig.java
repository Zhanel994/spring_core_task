package com.gym;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

//configuration class for cucumber and spring integration
@CucumberContextConfiguration
@SpringBootTest(classes = GymApplication.class)
public class CucumberSpringConfig {
}
