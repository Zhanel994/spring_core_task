package com.gym.dao;

import com.gym.models.Training;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;

//DAO for training entity
@Repository
@Transactional
public class TrainingDao {

    @PersistenceContext
    private EntityManager entityManager;

    //saves or updates training
    public void save(Training t) {
        if (t.getId() == null) entityManager.persist(t);
        else entityManager.merge(t);
    }

    //finds training by id
    public Training findById(Long id) {
        return entityManager.find(Training.class, id);
    }

    //finds trainings by trainee
    public List<Training> findTraineeTrainings(
            String username,
            LocalDate from,
            LocalDate to,
            String trainerName,
            String trainingType) {

        return entityManager.createQuery(
                        "SELECT t FROM Training t " +
                                "WHERE t.trainee.user.username = :u " +
                                "AND (:from IS NULL OR t.trainingDate >= :from) " +
                                "AND (:to IS NULL OR t.trainingDate <= :to) " +
                                "AND (:trainer IS NULL OR LOWER(t.trainer.user.firstName)=LOWER(:trainer)) " +
                                "AND (:type IS NULL OR LOWER(t.trainingType.trainingTypeName)=LOWER(:type))",
                        Training.class)
                .setParameter("u", username)
                .setParameter("from", from)
                .setParameter("to", to)
                .setParameter("trainer", trainerName)
                .setParameter("type", trainingType)
                .getResultList();
    }

    //finds trainings by trainer
    public List<Training> findTrainerTrainings(String username,
                                               LocalDate from,
                                               LocalDate to,
                                               String traineeName) {
        return entityManager.createQuery(
                        "SELECT t FROM Training t " +
                                "WHERE t.trainer.user.username = :u " +
                                "AND (:from IS NULL OR t.trainingDate >= :from) " +
                                "AND (:to IS NULL OR t.trainingDate <= :to) " +
                                "AND (:trainee IS NULL OR " +
                                "LOWER(t.trainee.user.firstName)=LOWER(:trainee))",
                        Training.class)
                .setParameter("u", username)
                .setParameter("from", from)
                .setParameter("to", to)
                .setParameter("trainee", traineeName)
                .getResultList();
    }
}
