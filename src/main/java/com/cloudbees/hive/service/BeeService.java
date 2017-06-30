package com.cloudbees.hive.service;

import com.cloudbees.hive.model.Bee;
import com.cloudbees.hive.repository.BeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Adrien Lecharpentier
 */
@Service
public class BeeService {
    private final BeeRepository repository;

    @Autowired
    public BeeService(BeeRepository repository) {
        this.repository = repository;
    }

    public Bee add(Bee bee) {
        return this.repository.save(bee);
    }

    public Iterable<Bee> all() {
        return this.repository.findAll();
    }

    public Optional<Bee> byId(String id) {
        return this.repository.findById(id);
    }
}
