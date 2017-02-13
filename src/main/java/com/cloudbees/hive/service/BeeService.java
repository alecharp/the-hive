package com.cloudbees.hive.service;

import com.cloudbees.hive.model.Bee;
import com.cloudbees.hive.repository.BeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Adrien Lecharpentier
 */
@Service
public class BeeService {
    private final BeeRepository beeRepository;

    @Autowired
    public BeeService(BeeRepository beeRepository) {
        this.beeRepository = beeRepository;
    }

    public Bee addBee(Bee bee) {
        return this.beeRepository.addBee(bee);
    }

    public List<Bee> getAll() {
        return this.beeRepository.getHive();
    }
}
