package com.cloudbees.hive.service;

import com.cloudbees.hive.repository.BeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Adrien Lecharpentier
 */
@Service
public class LocationService {
    private final BeeRepository repository;

    @Autowired
    public LocationService(BeeRepository repository) {
        this.repository = repository;
    }

    public Iterable<String> all() {
        return repository.findAllDistinctLocation();
    }
}
