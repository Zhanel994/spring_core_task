package com.gym.dao;

import com.gym.models.Trainer;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

//DAO for trainer entity
@Repository
@Transactional
public class TrainerDao {

    @PersistenceContext
    private EntityManager entityManager;

    //saves or updates trainer
    public void save(Trainer trainer) {

        if (trainer.getId() == null)
            entityManager.persist(trainer);
        else
            entityManager.merge(trainer);
    }

    //finds by id
    public Trainer findById(Long id) {
        return entityManager.find(Trainer.class, id);
    }

    //finds trainer by username
    public Trainer findByUsername(String username) {

        return entityManager.createQuery(
                                "SELECT t FROM Trainer t " +
                                        "WHERE t.user.username = :u", Trainer.class)
                        .setParameter("u", username)
                        .getResultStream()
                        .findFirst()
                        .orElse(null);
    }

    //deletes trainer by id
    public void delete(Long id) {
        Trainer trainer = findById(id);
        if (trainer != null) entityManager.remove(trainer);
    }
}
