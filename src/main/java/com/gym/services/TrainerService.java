package com.gym.services;

import com.gym.dao.TrainerDao;
import com.gym.exceptions.EntityNotFoundException;
import com.gym.exceptions.ValidationException;
import com.gym.models.Trainer;
import com.gym.models.User;
import com.gym.utils.PasswordGenerator;
import com.gym.utils.UsernameGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

//service for trainer business logic
@Service
@Transactional
public class TrainerService {
    private static final Logger log = LoggerFactory.getLogger(TrainerService.class);

    private final TrainerDao trainerDao;
    private final UsernameGenerator usernameGenerator;
    private final PasswordGenerator passwordGenerator;

    @PersistenceContext
    private EntityManager entityManager;

    public TrainerService(TrainerDao trainerDao, UsernameGenerator usernameGenerator, PasswordGenerator passwordGenerator) {
        this.trainerDao = trainerDao;
        this.usernameGenerator = usernameGenerator;
        this.passwordGenerator = passwordGenerator;
    }

    //creates trainer
    public Trainer create(Trainer trainer) {
        validate(trainer);

        User user = trainer.getUser();

        user.setUsername(usernameGenerator.generate(user.getFirstName(), user.getLastName()));
        user.setPassword(passwordGenerator.generate());
        user.setActive(true);

        trainerDao.save(trainer);

        log.info("Trainer created: {}", user.getUsername());

        return trainer;
    }

    //updates existing trainer
    public Trainer update(Trainer trainer) {
        validate(trainer);

        trainerDao.save(trainer);

        log.info("Trainer updated: id={}", trainer.getId());

        return trainer;
    }

    //gets trainer by id
    public Trainer get(Long id) {
        return trainerDao.findById(id);
    }

    public void changePassword(String username, String newPassword) {
        Trainer trainer = trainerDao.findByUsername(username);

        if (trainer == null) {
            throw new EntityNotFoundException("Trainer not found!");
        }

        trainer.getUser().setPassword(newPassword);

        trainerDao.save(trainer);
    }

    public void activate(String username) {
        Trainer trainer = trainerDao.findByUsername(username);

        if (trainer == null) {
            throw new EntityNotFoundException("Trainer not found!");
        }

        if(trainer.getUser().isActive()) {
            throw new RuntimeException("Already active!");
        }

        trainer.getUser().setActive(true);
        trainerDao.save(trainer);
    }

    public void deactivate(String username) {
        Trainer trainer = trainerDao.findByUsername(username);

        if (trainer == null) {
            throw new EntityNotFoundException("Trainer not found!");
        }

        if(!trainer.getUser().isActive()) {
            throw new RuntimeException("Already inactive!");
        }

        trainer.getUser().setActive(false);

        trainerDao.save(trainer);
    }

    public Trainer getByUsername(String username) {
        return trainerDao.findByUsername(username);
    }

    private void validate(Trainer trainer) {
        User user = trainer.getUser();

        if (user == null) {
            throw new ValidationException("User required!");
        }

        if (user.getFirstName() == null || user.getFirstName().isBlank()) {
            throw new ValidationException("First name required!");
        }

        if (user.getLastName() == null || user.getLastName().isBlank()) {
            throw new ValidationException("Last name required");
        }
    }
}