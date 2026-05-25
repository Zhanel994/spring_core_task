package com.gym.dao;

import com.gym.models.Trainee;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

//DAO for trainee entity
@Repository
@Transactional
public class TraineeDao {

    @PersistenceContext
    private EntityManager entityManager;

    //saves or updates trainee
    public void save(Trainee trainee) {
        if (trainee.getId() == null) entityManager.persist(trainee);
        else entityManager.merge(trainee);
    }

    //finds trainee by id
    public Trainee findById(Long id) {
        return entityManager.find(Trainee.class, id);
    }

    //deletes trainee by id
    public void delete(Long id) {
        Trainee trainee = findById(id);
        if (trainee != null) entityManager.remove(trainee);
    }

    //finds trainee by username
    public Trainee findByUsername(String username) {

        return entityManager.createQuery(
                        "SELECT t FROM Trainee t " +
                                "WHERE t.user.username = :u", Trainee.class)
                .setParameter("u", username)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }
}
