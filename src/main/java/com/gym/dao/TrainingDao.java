package com.gym.dao;

import com.gym.models.Training;
import com.gym.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class TrainingDao {
    private Storage storage;

    @Autowired
    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public void save(Training training) {
        storage.getTrainings().put(training.getId(), training);
    }

    public Training findById(Long id) {
        return storage.getTrainings().get(id);
    }
}
