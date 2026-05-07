package com.gym.dao;

import com.gym.models.Trainee;
import com.gym.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class TraineeDao {
    private Storage storage;

    @Autowired
    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public void save(Trainee trainee) {
        storage.getTrainees().put(trainee.getId(), trainee);
    }

    public Trainee findById(Long id) {
        return storage.getTrainees().get(id);
    }

    public void delete(Long id) {
        storage.getTrainees().remove(id);
    }

    public List<Trainee> findAll() {
        return new ArrayList<>(storage.getTrainees().values());
    }
}
