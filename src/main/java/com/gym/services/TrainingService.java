package com.gym.services;

import com.gym.dao.TrainingDao;
import com.gym.models.Training;
import com.gym.storage.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrainingService {
    private static final Logger log = LoggerFactory.getLogger(TrainingService.class);

    @Autowired
    private TrainingDao trainingDao;

    @Autowired
    private Storage storage;

    public Training create(Training training) {
        training.setId(storage.generateTrainingId());
        trainingDao.save(training);

        log.info("Training created: id={}, name={}",
                training.getId(),
                training.getTrainingName());

        return training;
    }

    public Training get(Long id) {
        Training training = trainingDao.findById(id);

        log.info("Training fetched: id={}, found={}", id, training != null);

        return training;
    }
}
