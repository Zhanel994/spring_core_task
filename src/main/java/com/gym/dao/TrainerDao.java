package com.gym.dao;

import com.gym.models.Trainer;
import com.gym.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TrainerDao {
    private Storage storage;

    @Autowired
    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public void save(Trainer trainer) {
        storage.getTrainers().put(trainer.getId(), trainer);
    }

    public Trainer findById(Long id) {
        return storage.getTrainers().get(id);
    }
}
