package com.gym.services;

import com.gym.dao.TrainerDao;
import com.gym.models.Trainer;
import com.gym.storage.Storage;
import com.gym.utils.UsernameGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TrainerService {
    private static final Logger log = LoggerFactory.getLogger(TrainerService.class);

    @Autowired
    private TrainerDao trainerDao;

    @Autowired
    private UsernameGenerator usernameGenerator;

    @Autowired
    private Storage storage;

    public Trainer create(Trainer trainer) {
        trainer.setId(storage.generateTrainerId());
        trainer.setUsername(usernameGenerator.generate(trainer.getFirstName(), trainer.getLastName()));
        trainer.setPassword(generatePassword());

        trainerDao.save(trainer);

        log.info("Trainer created: id={}, username={}", trainer.getId(), trainer.getUsername());

        return trainer;
    }

    public Trainer update(Trainer trainer) {
        trainerDao.save(trainer);

        log.info("Trainer updated: id={}", trainer.getId());

        return trainer;
    }

    public Trainer get(Long id) {
        Trainer trainer = trainerDao.findById(id);

        log.info("Trainer fetched: id={}, found={}", id, trainer != null);

        return trainer;
    }

    private String generatePassword() {
        return UUID.randomUUID().toString().substring(0, 10);
    }
}
