package com.gym.services;

import com.gym.dao.TraineeDao;
import com.gym.dao.TrainerDao;
import com.gym.dto.responses.RegistrationResponse;
import com.gym.exceptions.EntityNotFoundException;
import com.gym.exceptions.ValidationException;
import com.gym.models.Trainee;
import com.gym.models.Trainer;
import com.gym.models.User;
import com.gym.utils.PasswordGenerator;
import com.gym.utils.UsernameGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.Collectors;

//service for trainee business logic
@Service
@Transactional
public class TraineeService {
    private static final Logger log = LoggerFactory.getLogger(TraineeService.class);

    private final TraineeDao traineeDao;
    private final TrainerDao trainerDao;
    private final UsernameGenerator usernameGenerator;
    private final PasswordGenerator passwordGenerator;
    private final PasswordEncoder passwordEncoder;

    @PersistenceContext
    private EntityManager entityManager;

    public TraineeService(TraineeDao traineeDao, TrainerDao trainerDao,
                          UsernameGenerator usernameGenerator, PasswordGenerator passwordGenerator, PasswordEncoder passwordEncoder) {
        this.traineeDao = traineeDao;
        this.trainerDao = trainerDao;
        this.usernameGenerator = usernameGenerator;
        this.passwordGenerator = passwordGenerator;
        this.passwordEncoder = passwordEncoder;
    }

    //creates new trainee
    public RegistrationResponse create(Trainee trainee) {
        validate(trainee);

        User user = trainee.getUser();

        String username = usernameGenerator.generate(
                user.getFirstName(),
                user.getLastName()
        );

        String rawPassword = passwordGenerator.generate();

        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setActive(true);

        traineeDao.save(trainee);

        return new RegistrationResponse(
                username,
                rawPassword
        );
    }

    //updates existing trainee
    public Trainee update(Trainee trainee) {
        validate(trainee);

        traineeDao.save(trainee);

        log.info("Trainee updated: id={}", trainee.getId());

        return trainee;
    }

    //deletes trainee by id
    public void delete(Long id) {
        traineeDao.delete(id);
        log.info("Trainee deleted: id={}", id);
    }

    //deletes trainee by username
    public void deleteByUsername(String username) {
        Trainee trainee = traineeDao.findByUsername(username);

        if (trainee == null) {
            throw new EntityNotFoundException("Trainee not found!");
        }

        traineeDao.delete(trainee.getId());

        log.info("Trainee deleted: {}", username);
    }

    //gets trainee by id
    public Trainee get(Long id) {
        Trainee trainee = traineeDao.findById(id);

        log.info("Trainee fetched: id={}", id);

        return trainee;
    }

    public void changePassword(String username, String newPassword) {
        Trainee trainee = traineeDao.findByUsername(username);

        if (trainee == null) {
            throw new EntityNotFoundException("Trainee not found!");
        }

        trainee.getUser().setPassword(passwordEncoder.encode(newPassword));
        traineeDao.save(trainee);

        log.info("Password changed for {}", username);
    }

    public void activate(String username) {
        Trainee trainee = traineeDao.findByUsername(username);

        if (trainee == null) {
            throw new EntityNotFoundException("Trainee not found!");
        }

        if (trainee.getUser().isActive()) {
            throw new RuntimeException("Already active!");
        }

        trainee.getUser().setActive(true);

        traineeDao.save(trainee);
    }

    public void deactivate(String username) {
        Trainee trainee = traineeDao.findByUsername(username);

        if (trainee == null) {
            throw new EntityNotFoundException("Trainee not found!");
        }

        if (!trainee.getUser().isActive()) {
            throw new RuntimeException("Already inactive!");
        }

        trainee.getUser().setActive(false);

        traineeDao.save(trainee);
    }

    //gets trainee by username
    public Trainee getByUsername(String username) {
        return traineeDao.findByUsername(username);
    }

    public List<Trainer> getUnassigned(String traineeUsername) {
        return entityManager.createQuery(
                        "SELECT tr FROM Trainer tr " +
                                "WHERE tr.id NOT IN (" +
                                "SELECT t.id FROM Trainee trn " +
                                "JOIN trn.trainers t " +
                                "WHERE trn.user.username=:u)",
                        Trainer.class
                )
                .setParameter("u", traineeUsername)
                .getResultList();
    }

    public void assignTrainer(String traineeUsername, Long trainerId) {
        Trainee trainee = traineeDao.findByUsername(traineeUsername);
        Trainer trainer = trainerDao.findById(trainerId);

        if (trainee == null || trainer == null) {
            throw new EntityNotFoundException("Trainer or trainee not found!");
        }

        if (!trainee.getTrainers().contains(trainer)) {
            trainee.getTrainers().add(trainer);
            trainer.getTrainees().add(trainee);
        }

        traineeDao.save(trainee);
        trainerDao.save(trainer);

        log.info("Trainer {} assigned to trainee {}", trainerId, traineeUsername);
    }

    public void updateTrainerList(String username, List<Long> ids) {
        Trainee trainee = traineeDao.findByUsername(username);

        if (trainee == null)
            throw new RuntimeException("Trainee not found!");

        List<Trainer> trainers =
                ids.stream()
                        .map(trainerDao::findById)
                        .collect(Collectors.toList());

        trainee.setTrainers(trainers);

        traineeDao.save(trainee);
    }

    private void validate(Trainee trainee) {
        User user = trainee.getUser();

        if (user == null) {
            throw new ValidationException("User required!");
        }

        if (user.getFirstName() == null || user.getFirstName().isBlank()) {
            throw new ValidationException("First name required!");
        }

        if (user.getLastName() == null || user.getLastName().isBlank()) {
            throw new ValidationException("Last name required!");
        }
    }
}