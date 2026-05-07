package com.gym.services;

import com.gym.dao.TraineeDao;
import com.gym.models.Trainee;
import com.gym.storage.Storage;
import com.gym.utils.UsernameGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TraineeService {
    private static final Logger log = LoggerFactory.getLogger(TraineeService.class);

    private TraineeDao traineeDao;

    @Autowired
    private Storage storage;

    @Autowired
    private UsernameGenerator usernameGenerator;

    @Autowired
    public void setTraineeDao(TraineeDao traineeDao) {
        this.traineeDao = traineeDao;
    }

    public Trainee create(Trainee trainee) {
        trainee.setId(storage.generateTraineeId());
        trainee.setUsername(usernameGenerator.generate(trainee.getFirstName(), trainee.getLastName()));
        trainee.setPassword(generatePassword());

        traineeDao.save(trainee);

        log.info("Trainee created: id={}, username={}",
                trainee.getId(),
                trainee.getUsername());

        return trainee;
    }

    public Trainee update(Trainee trainee) {
        traineeDao.save(trainee);

        log.info("Trainee updated: id={}", trainee.getId());

        return trainee;
    }

    public void delete(Long id) {
        traineeDao.delete(id);

        log.info("Trainee deleted: id={}", id);
    }

    public Trainee get(Long id) {
        Trainee trainee = traineeDao.findById(id);

        log.info("Trainee fetched: id={}, found={}", id, trainee != null);

        return trainee;
    }

    private String generatePassword() {
        return UUID.randomUUID().toString().substring(0, 10);
    }
}
