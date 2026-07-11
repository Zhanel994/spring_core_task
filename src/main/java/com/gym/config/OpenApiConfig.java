package com.gym.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//for swagger
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        OpenAPI configuration = new OpenAPI();

        Info info = new Info();

        info.setTitle("Gym Application");
        info.setVersion("1.0");

        configuration.setInfo(info);

        return configuration;
    }
}
