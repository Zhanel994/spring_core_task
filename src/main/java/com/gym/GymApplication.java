package com.gym;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.jms.annotation.EnableJms;

//application entry point
@SpringBootApplication
@EnableEurekaServer
@EnableFeignClients
@EnableJms
public class GymApplication {
    public static void main(String[] args) {
        SpringApplication.run(GymApplication.class, args);
    }
}