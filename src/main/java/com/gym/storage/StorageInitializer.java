package com.gym.storage;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class StorageInitializer implements BeanPostProcessor {
    @Value("${storage.init.file}")
    private String filePath;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if(bean instanceof Storage) {
            System.out.println("Initializing storage from: " + filePath);
        }
        return bean;
    }
}
