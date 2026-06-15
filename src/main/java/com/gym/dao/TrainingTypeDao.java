package com.gym.dao;

import com.gym.models.TrainingType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class TrainingTypeDao {

    @PersistenceContext
    private EntityManager em;

    public TrainingType findByName(String name) {
        return em.createQuery(
                        "SELECT t FROM TrainingType t WHERE LOWER(t.trainingTypeName)=LOWER(:n)",
                        TrainingType.class)
                .setParameter("n", name)
                .getResultStream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("TrainingType not found"));
    }
}
